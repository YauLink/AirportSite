package com.myapp.Airports.exceptions;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(String ticketNo) {
        super("Ticket '" + ticketNo + "' was not found.");
    }

    public TicketNotFoundException(String message, boolean customMessage) {
        super(message);
    }
}