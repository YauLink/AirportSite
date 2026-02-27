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

    public AuthResponseDTO() {}

    public AuthResponseDTO(Long userId, String fullName) {
        this.userId = userId;
        this.fullName = fullName;
    }

    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
