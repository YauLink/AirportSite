package com.myapp.Airports.dto;

public class TicketDTO {

    private String ticketNo;
    private String bookRef;
    private String passengerId;
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
