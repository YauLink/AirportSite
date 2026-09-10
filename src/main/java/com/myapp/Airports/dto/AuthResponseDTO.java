package com.myapp.Airports.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthResponseDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be up to 100 characters")
    private String fullName;

    private String message;

    private String token;

    public AuthResponseDTO() {}

    public AuthResponseDTO(
            Long userId,
            String fullName,
            String message) {

        this.userId = userId;
        this.fullName = fullName;
        this.message = message;
    }

    public AuthResponseDTO(
            Long userId,
            String fullName,
            String message,
            String token) {

        this.userId = userId;
        this.fullName = fullName;
        this.message = message;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
