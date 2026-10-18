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

    public AuthService(
            RestTemplate restTemplate,
            @Value("${user-management.auth-url}") String authUrl) {
        this.restTemplate = restTemplate;
        this.authUrl = authUrl;
    }

    @Override
    public AuthResponseDTO login(
            String username,
            String password) {

        AuthRequestDTO request = new AuthRequestDTO(username, password);

        try {

            return restTemplate.postForObject(
                    authUrl,
                    request,
                    AuthResponseDTO.class
            );

        } catch (RestClientResponseException e) {

            HttpStatusCode status =
                    e.getStatusCode();

            if (status.value() == 401
                    || status.value() == 404) {

                throw new UserNotFoundException(
                        "Invalid username or password",
                        true
                );
            }

            throw e;
        }
    }
}
