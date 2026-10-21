package com.myapp.Airports.model;

/**
 * Authenticated user data extracted from a JSON Web Token.
 * <p>
 * Provides the identity and role information used by the application security
 * layer.
 * </p>
 */
public class JwtUserPrincipal {

    private final Long userId;
    private final String username;
    private final String fullName;
    private final String role;

    public JwtUserPrincipal(
            Long userId,
            String username,
            String fullName,
            String role) {

        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }
}
