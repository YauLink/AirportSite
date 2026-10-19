package com.myapp.Airports.security;

import com.myapp.Airports.model.JwtUserPrincipal;
import com.myapp.Airports.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticatesFromAirportsCookie() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        JwtUserPrincipal user = new JwtUserPrincipal(7L, "john", "John Doe", "USER");

        when(jwtService.extractUser("token")).thenReturn(user);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new jakarta.servlet.http.Cookie("AIRPORTS_JWT", "token"));

        filter.doFilterInternal(
                request,
                new MockHttpServletResponse(),
                new MockFilterChain()
        );

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("USER", SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void ignoresUnsupportedRole() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        when(jwtService.extractUser("token"))
                .thenReturn(new JwtUserPrincipal(7L, "john", "John Doe", "SUPERUSER"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        filter.doFilterInternal(
                request,
                new MockHttpServletResponse(),
                new MockFilterChain()
        );

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
