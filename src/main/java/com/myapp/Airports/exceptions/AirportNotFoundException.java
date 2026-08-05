package com.myapp.Airports.exceptions;

public class AirportNotFoundException extends RuntimeException {

    public AirportNotFoundException(Long id) {
        super("Airport with id " + id + " was not found.");
    }

    public AirportNotFoundException(String message) {
        super(message);
    }
}