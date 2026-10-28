package com.myapp.Airports.service;

import com.myapp.Airports.dto.AuthRequestDTO;
import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.exceptions.UserNotFoundException;
import com.myapp.Airports.storage.api.IAuthService;
import org.springframework.http.HttpStatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

/**
 * Handles credentials verification and login.
 */
@Service
public class AuthService implements IAuthService {

    private final RestTemplate restTemplate;
    private final String authUrl;
    private final LoginRateLimiter loginRateLimiter;

    public AuthService(
            RestTemplate restTemplate,
            @Value("${user-management.auth-url}") String authUrl,
            LoginRateLimiter loginRateLimiter) {
        this.restTemplate = restTemplate;
        this.authUrl = authUrl;
        this.loginRateLimiter = loginRateLimiter;
    }

    @Override
    public AuthResponseDTO login(
            String username,
            String password) {

        loginRateLimiter.checkAllowed(username);
        AuthRequestDTO request = new AuthRequestDTO(username, password);

        try {

            AuthResponseDTO response = restTemplate.postForObject(
                    authUrl,
                    request,
                    AuthResponseDTO.class
            );
            if (response == null) {
                loginRateLimiter.recordFailure(username);
                throw new UserNotFoundException("Invalid username or password", true);
            }
            if (response.getUserId() == null) {
                loginRateLimiter.recordFailure(username);
            } else {
                loginRateLimiter.reset(username);
            }
            return response;

        } catch (RestClientResponseException e) {

            HttpStatusCode status =
                    e.getStatusCode();

            if (status.value() == 401
                    || status.value() == 404) {
                loginRateLimiter.recordFailure(username);

                throw new UserNotFoundException(
                        "Invalid username or password",
                        true
                );
            }

            throw e;
        }
    }
}
