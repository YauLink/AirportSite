package com.myapp.Airports.storage.api;

import com.myapp.Airports.dto.AuthResponseDTO;

public interface IAuthService {

    AuthResponseDTO login(String username, String password);
}
