package com.myapp.Airports.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Represents a flight ticket in the system.
 * <p>
 * Each ticket links a user to a specific ticket and stores
 * ticket metadata such as passenger id and booking.
 * </p>
 */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @Column(name = "ticket_no", nullable = false)
    private String ticketNo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_ref", referencedColumnName = "book_ref")
    private Booking booking;

    @Column(name = "passenger_id", nullable = false)
    private String passengerId;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @Column(name = "contact_data")
    @JdbcTypeCode(SqlTypes.JSON)
    private String contactData;

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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
