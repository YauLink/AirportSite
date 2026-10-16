package com.myapp.Airports.service;

import com.myapp.Airports.model.JwtUserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = System.getProperty("JWT_SECRET");

    @Test
    void extractsTheUserManagementTokenContract() {
        JwtService service = new JwtService(SECRET);

        String token = Jwts.builder()
                .setSubject("john")
                .claim("userId", 7L)
                .claim("fullName", "John Doe")
                .claim("role", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(
                        Keys.hmacShaKeyFor(
                                io.jsonwebtoken.io.Decoders.BASE64.decode(SECRET)
                        ),
                        SignatureAlgorithm.HS256
                )
                .compact();

        JwtUserPrincipal user = service.extractUser(token);

        assertEquals(7L, user.getUserId());
        assertEquals("john", user.getUsername());
        assertEquals("John Doe", user.getFullName());
        assertEquals("USER", user.getRole());
        assertTrue(service.isTokenValid(token));
    }

    @Test
    void rejectsTokenWithMissingRequiredClaim() {
        JwtService service = new JwtService(SECRET);

        String token = Jwts.builder()
                .setSubject("john")
                .claim("userId", 7L)
                .claim("role", "USER")
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(
                        Keys.hmacShaKeyFor(
                                io.jsonwebtoken.io.Decoders.BASE64.decode(SECRET)
                        ),
                        SignatureAlgorithm.HS256
                )
                .compact();

        assertFalse(service.isTokenValid(token));
        assertThrows(IllegalArgumentException.class,
                () -> service.extractUser(token));
    }
}
