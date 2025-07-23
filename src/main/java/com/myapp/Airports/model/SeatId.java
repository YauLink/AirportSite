package com.myapp.Airports.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SeatId implements Serializable {

    private String seatNo;
    private String aircraftCode;

    public SeatId() {
    }

    public SeatId(String seatNo, String aircraftCode) {
        this.seatNo = seatNo;
        this.aircraftCode = aircraftCode;
    }

    // Getters, setters, equals, and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeatId)) return false;
        SeatId that = (SeatId) o;
        return Objects.equals(seatNo, that.seatNo) &&
                Objects.equals(aircraftCode, that.aircraftCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatNo, aircraftCode);
    }
}