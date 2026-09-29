package com.myapp.Airports.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardingPassId that)) return false;
        return Objects.equals(ticketNo, that.ticketNo)
                && Objects.equals(flightId, that.flightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketNo, flightId);
    }
}
