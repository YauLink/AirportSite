package com.myapp.Airports.exceptions;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(String bookingRef) {
        super("Booking with reference '" + bookingRef + "' was not found.");
    }

    public BookingNotFoundException(String message, boolean customMessage) {
        super(message);
    }
}