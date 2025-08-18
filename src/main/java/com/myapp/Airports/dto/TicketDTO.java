package com.myapp.Airports.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class TicketDTO {

    @NotBlank(message = "Ticket number is required")
    @Size(max = 13, message = "Ticket number must be up to 13 characters")
    private String ticketNo;

    @NotBlank(message = "Booking reference is required")
    @Size(max = 6, message = "Booking reference must be up to 6 characters")
    private String bookRef;

    @NotBlank(message = "Passenger ID is required")
    private String passengerId;

    @NotBlank(message = "Passenger name is required")
    @Size(max = 100, message = "Passenger name must be up to 100 characters")
    private String passengerName;


    private String contactData;

    public TicketDTO() {}

    public TicketDTO(String ticketNo, String bookRef, String passengerId, String passengerName, String contactData) {
        this.ticketNo = ticketNo;
        this.bookRef = bookRef;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.contactData = contactData;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getBookRef() {
        return bookRef;
    }

    public void setBookRef(String bookRef) {
        this.bookRef = bookRef;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getContactData() {
        return contactData;
    }

    public void setContactData(String contactData) {
        this.contactData = contactData;
    }
}
