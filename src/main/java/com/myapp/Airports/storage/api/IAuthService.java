package com.myapp.Airports.storage.api;

import com.myapp.Airports.dto.AuthResponseDTO;

/**
 * Defines the authentication operations exposed to application clients.
 */
public interface IAuthService {

    AuthResponseDTO login(String username, String password);
}
