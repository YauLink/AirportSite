package com.myapp.Airports.model;

import jakarta.persistence.*;

@Entity
@Table(name = "boarding_passes", schema = "bookings")
public class BoardingPass {

    @EmbeddedId
    private BoardingPassId id;

    @Column(name = "boarding_no")
    private Integer boardingNo;

    @Column(name = "seat_no")
    private String seatNo;

    public BoardingPassId getId() {
        return id;
    }

    public void setId(BoardingPassId id) {
        this.id = id;
    }

    public Integer getBoardingNo() {
        return boardingNo;
    }

    public void setBoardingNo(Integer boardingNo) {
        this.boardingNo = boardingNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public void setFLightId(Integer flightId) {
        if (this.id == null) {
            this.id = new BoardingPassId();
        }
        this.id.setFlightId(flightId);
    }

    public void setTicketNo(String ticketNo) {
        if (this.id == null) {
            this.id = new BoardingPassId();
        }
        this.id.setTicketNo(ticketNo);
    }

    public Integer getFlightId() {
        return this.id != null ? this.id.getFlightId() : null;
    }

    public String getTicketNo() {
        return this.id != null ? this.id.getTicketNo() : null;
    }
}
