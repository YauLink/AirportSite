package com.myapp.Airports.model;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_flights")
public class TicketFlight {

    @EmbeddedId
    private TicketFlightId id;

    @ManyToOne
    @MapsId("ticketNo")
    @JoinColumn(name = "ticket_no")
    private Ticket ticket;

    @ManyToOne
    @MapsId("flightId")
    @JoinColumn(name = "flight_id")
    private Flying flying;

    @Column(name = "fare_conditions", nullable = false)
    private String fareConditions;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    public TicketFlightId getId() {
        return id;
    }

    public void setId(TicketFlightId id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Flying getFlying() {
        return flying;
    }

    public void setFlying(Flying flying) {
        this.flying = flying;
    }

    public String getFareConditions() {
        return fareConditions;
    }

    public void setFareConditions(String fareConditions) {
        this.fareConditions = fareConditions;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
