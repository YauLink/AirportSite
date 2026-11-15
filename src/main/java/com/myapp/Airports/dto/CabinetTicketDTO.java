package com.myapp.Airports.dto;

public class CabinetTicketDTO {
    private final String ticketNo;
    private final String bookingRef;
    private final String passengerName;
    private final String contactData;

    public CabinetTicketDTO(String ticketNo, String bookingRef, String passengerName, String contactData) {
        this.ticketNo = ticketNo;
        this.bookingRef = bookingRef;
        this.passengerName = passengerName;
        this.contactData = contactData;
    }

    public String getTicketNo() { return ticketNo; }
    public String getBookingRef() { return bookingRef; }
    public String getPassengerName() { return passengerName; }
    public String getContactData() { return contactData; }
}
