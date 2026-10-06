package com.myapp.Airports.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/** Converts application exceptions into consistent JSON responses for REST clients. */
@RestControllerAdvice(basePackages = "com.myapp.Airports.controller.rest")
public class GlobalRestExceptionHandler {

    @ExceptionHandler({
            FlightNotFoundException.class,
            AirportNotFoundException.class,
            BookingNotFoundException.class,
            TicketNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        return error(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(SeatUnavailableException.class)
    public ResponseEntity<ApiError> handleSeatUnavailable(SeatUnavailableException ex) {
        return error(HttpStatus.CONFLICT, "SEAT_UNAVAILABLE", ex.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(EntityAlreadyExistsException ex) {
        return error(HttpStatus.CONFLICT, "ENTITY_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ApiError> handleUnauthenticated(UserNotAuthenticatedException ex) {
        return error(HttpStatus.UNAUTHORIZED, "UNAUTHENTICATED", ex.getMessage());
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<ApiError> handleInvalidBookingState(InvalidBookingStateException ex) {
        return error(HttpStatus.BAD_REQUEST, "INVALID_BOOKING_STATE", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return error(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return error(HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred.");
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String code, String message) {
        String safeMessage = message == null || message.isBlank()
                ? status.getReasonPhrase()
                : message;
        return ResponseEntity.status(status)
                .body(ApiError.of(status.value(), code, safeMessage));
    }
}
