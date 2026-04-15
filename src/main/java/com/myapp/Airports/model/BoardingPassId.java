package com.myapp.Airports.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

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
