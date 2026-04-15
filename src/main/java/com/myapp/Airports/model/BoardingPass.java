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

    public void setFLightId (Integer id) {
        this.id.setFlightId(id);
    }

    public void setTicketNo (String number) {
        this.id.setTicketNo(number);
    }

}
