package com.myapp.Airports.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TicketFlightId implements Serializable {

    @Column(name = "ticket_no")
    private String  ticketNo;

    @Column(name = "flight_id")
    private Integer flightId;

    public TicketFlightId() {}

    public TicketFlightId(String  ticketNo, Integer flightId) {
        this.ticketNo = ticketNo;
        this.flightId = flightId;
    }

    public String  getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String  ticketNo) {
        this.ticketNo = ticketNo;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketFlightId that = (TicketFlightId) o;
        return Objects.equals(ticketNo, that.ticketNo) &&
                Objects.equals(flightId, that.flightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketNo, flightId);
    }
}
