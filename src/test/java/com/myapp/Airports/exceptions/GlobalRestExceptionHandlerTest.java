package com.myapp.Airports.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalRestExceptionHandlerTest {

    private final GlobalRestExceptionHandler handler = new GlobalRestExceptionHandler();

    @Test
    void seatConflictReturns409AndStableErrorCode() {
        var response = handler.handleSeatUnavailable(new SeatUnavailableException("12A"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("SEAT_UNAVAILABLE", response.getBody().code());
        assertEquals("Seat '12A' is unavailable.", response.getBody().message());
    }

    @Test
    void missingBookingReturns404() {
        var response = handler.handleNotFound(new BookingNotFoundException("ABC123"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("RESOURCE_NOT_FOUND", response.getBody().code());
    }

    @Test
    void unauthenticatedRequestReturns401() {
        var response = handler.handleUnauthenticated(
                new UserNotAuthenticatedException("User not authenticated"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("UNAUTHENTICATED", response.getBody().code());
    }

    @Test
    void malformedRequestReturns400() {
        var response = handler.handleMalformedRequest(
                new HttpMessageNotReadableException("invalid json", (org.springframework.http.HttpInputMessage) null));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("INVALID_REQUEST", response.getBody().code());
        assertEquals("The request could not be understood.", response.getBody().message());
    }
}
