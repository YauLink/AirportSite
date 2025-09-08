package com.myapp.Airports.model;

import jakarta.persistence.*;

@Entity
public class Seat {

    @EmbeddedId
    private SeatId id;

    @ManyToOne
    @MapsId("aircraftCode")
    @JoinColumn(name = "aircraft_code")
    private Aircraft aircraft;

    @Column(name = "fare_conditions")
    private String fareConditions;

    public Seat() {
    }

    public Seat(SeatId id, Aircraft aircraft, String fareConditions) {
        this.id = id;
        this.aircraft = aircraft;
        this.fareConditions = fareConditions;
    }

    public SeatId getId() {
        return id;
    }

    public void setId(SeatId id) {
        this.id = id;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public String getFareConditions() {
        return fareConditions;
    }

    public void setFareConditions(String fareConditions) {
        this.fareConditions = fareConditions;
    }
}