package com.myapp.Airports.exceptions;

public class InvalidBookingStateException extends RuntimeException {

    public InvalidBookingStateException(String message) {
        super(message);
    }
}
