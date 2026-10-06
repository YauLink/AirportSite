package com.myapp.Airports.exceptions;

import java.time.Instant;

/** Stable error payload returned by REST endpoints. */
public record ApiError(
        Instant timestamp,
        int status,
        String code,
        String message) {

    public static ApiError of(int status, String code, String message) {
        return new ApiError(Instant.now(), status, code, message);
    }
}
