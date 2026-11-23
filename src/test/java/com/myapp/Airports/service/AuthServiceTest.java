package com.myapp.Airports.service;

import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.exceptions.TooManyLoginAttemptsException;
import com.myapp.Airports.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private LoginRateLimiter loginRateLimiter;

    private AuthService service;

    @BeforeEach
    void setUp() {
        service = new AuthService(restTemplate, "http://user-management/login", loginRateLimiter);
    }

    @Test
    void successfulLoginResetsRateLimit() {
        AuthResponseDTO response = new AuthResponseDTO(7L, "John Doe", "ok", "jwt");
        when(restTemplate.postForObject(eq("http://user-management/login"), any(), eq(AuthResponseDTO.class)))
                .thenReturn(response);

        assertSame(response, service.login(" John ", "secret"));

        verify(loginRateLimiter).checkAllowed(" John ");
        verify(loginRateLimiter).reset(" John ");
        verify(loginRateLimiter, never()).recordFailure(any());
    }

    @Test
    void nullUpstreamResponseIsTreatedAsInvalidCredentials() {
        when(restTemplate.postForObject(anyString(), any(), eq(AuthResponseDTO.class)))
                .thenReturn(null);

        assertThrows(UserNotFoundException.class,
                () -> service.login("john", "bad"));

        verify(loginRateLimiter).recordFailure("john");
    }

    @Test
    void invalidCredentialResponseRecordsFailure() {
        when(restTemplate.postForObject(anyString(), any(), eq(AuthResponseDTO.class)))
                .thenReturn(new AuthResponseDTO(null, null, "invalid"));

        AuthResponseDTO response = service.login("john", "bad");

        assertNull(response.getUserId());
        verify(loginRateLimiter).recordFailure("john");
        verify(loginRateLimiter, never()).reset(any());
    }

    @Test
    void unauthorizedAndNotFoundUpstreamResponsesBecomeGenericAuthErrors() {
        for (HttpStatus status : new HttpStatus[]{HttpStatus.UNAUTHORIZED, HttpStatus.NOT_FOUND}) {
            reset(restTemplate, loginRateLimiter);
            when(restTemplate.postForObject(anyString(), any(), eq(AuthResponseDTO.class)))
                    .thenThrow(HttpClientErrorException.create(status, "error", HttpHeaders.EMPTY, new byte[0], null));

            assertThrows(UserNotFoundException.class,
                    () -> service.login("john", "bad"));
            verify(loginRateLimiter).recordFailure("john");
        }
    }

    @Test
    void unexpectedUpstreamErrorsAreNotHidden() {
        HttpServerErrorException failure = HttpServerErrorException.create(
                HttpStatus.BAD_GATEWAY, "upstream", HttpHeaders.EMPTY, new byte[0], null);
        when(restTemplate.postForObject(anyString(), any(), eq(AuthResponseDTO.class)))
                .thenThrow(failure);

        assertSame(failure, assertThrows(HttpServerErrorException.class,
                () -> service.login("john", "secret")));
        verify(loginRateLimiter, never()).recordFailure("john");
    }

    @Test
    void lockedAccountIsRejectedBeforeCallingUpstream() {
        doThrow(new TooManyLoginAttemptsException()).when(loginRateLimiter).checkAllowed("john");

        assertThrows(TooManyLoginAttemptsException.class,
                () -> service.login("john", "secret"));
        verifyNoInteractions(restTemplate);
    }
}
