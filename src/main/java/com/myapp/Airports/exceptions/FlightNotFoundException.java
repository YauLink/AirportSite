package com.myapp.Airports.exceptions;

public class FlightNotFoundException extends RuntimeException {

    public FlightNotFoundException(Long id) {
        super("Flight with id " + id + " was not found.");
    }

    public FlightNotFoundException(String message) {
        super(message);
    }
}