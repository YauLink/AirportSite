package com.myapp.Airports.exceptions;

public class SeatUnavailableException extends RuntimeException {

    public SeatUnavailableException(String seatNo) {
        super("Seat '" + seatNo + "' is unavailable.");
    }

    public SeatUnavailableException(String message, boolean customMessage) {
        super(message);
    }
}