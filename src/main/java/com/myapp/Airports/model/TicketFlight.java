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
    private Flying flight;

    @Column(name = "fare_conditions", nullable = false)
    private String fareConditions;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    private String seatNo;

    public TicketFlight() {}

    public TicketFlight(Long ticketNo, Long flightId, String fareConditions, BigDecimal amount, String seatNo) {
        this.fareConditions = fareConditions;
        this.amount = amount;
        this.seatNo = seatNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
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
