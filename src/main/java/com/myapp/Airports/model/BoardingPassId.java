package com.myapp.Airports.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

/**
 * Represents a composite identifier for a boarding pass.
 * <p>
 * Combines ticket number and flight id to uniquely
 * identify a boarding pass for a specific flight.
 * </p>
 */
@Embeddable
public class BoardingPassId implements Serializable {

    @Column(name = "ticket_no")
    private String ticketNo;

    @Column(name = "flight_id")
    private Integer flightId;

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }
}
