package com.myapp.Airports.dto;

public class AuthResponseDTO {

    private Long userId;
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
