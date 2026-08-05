package com.myapp.Airports.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super("User '" + username + "' was not found.");
    }

    public UserNotFoundException(String message, boolean customMessage) {
        super(message);
    }
}
