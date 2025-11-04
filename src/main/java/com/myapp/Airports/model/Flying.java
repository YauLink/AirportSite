package com.myapp.Airports.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "Flying")
@Table(name = "flights", schema = "bookings")
public class Flying implements Serializable{

    @Id
    @Column(name = "flight_id")
    private Integer flightId; //Flgiht ID

    @Column(name = "flight_no")
    private String flightNo; //Flight number

    @Column(name = "scheduled_departure")
    private LocalDateTime scheduledDeparture; //Departure Time

    @Column(name = "scheduled_arrival")
    private LocalDateTime scheduledArrival; //Arrival time

    @Column(name = "departure_airport")
    private String departureAirport; //Departure airport

    @Column(name = "arrival_airport")
    private String arrivalAirport; //Destination airport

    @Column(name = "status")
    private String status; //Flight status

    @ManyToOne
    @JoinColumn(name = "aircraft_code", insertable = false, updatable = false)
    private Aircraft aircraft;

    @Column(name = "aircraft_code")
    private String aircraftCode; //Aircraft code, IATA

    @Column(name = "actual_departure")
    private LocalDateTime  actualDeparture; //Departure time

    @Column(name = "actual_arrival")
    private LocalDateTime  actualArrival; //Arrival time

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public LocalDateTime getScheduledDeparture() {
        return scheduledDeparture;
    }

    public void setScheduledDeparture(LocalDateTime scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    public LocalDateTime getScheduledArrival() {
        return scheduledArrival;
    }

    public void setScheduledArrival(LocalDateTime scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }

    public LocalDateTime  getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(LocalDateTime  actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public LocalDateTime  getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(LocalDateTime  actualArrival) {
        this.actualArrival = actualArrival;
    }

    public void setFlightNumber(String aa123) {

    }

    public void setId(Integer id) {
    }
}
