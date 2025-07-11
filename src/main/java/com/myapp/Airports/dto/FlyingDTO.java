package com.myapp.Airports.dto;

import java.time.LocalDateTime;

public class FlyingDTO {

    //@NotNull(message = "Flight ID is required")
    private Integer flightId;

    //@NotBlank(message = "Flight number is required")
    //@Size(max = 10, message = "Flight number must be up to 10 characters")
    private String flightNo;

    //@NotNull(message = "Scheduled departure time is required")
    private LocalDateTime scheduledDeparture;

    //@NotNull(message = "Scheduled arrival time is required")
    private LocalDateTime scheduledArrival;

    //@NotBlank(message = "Departure airport code is required")
    //@Pattern(regexp = "^[A-Z]{3}$", message = "Departure airport code must be 3 uppercase letters")
    private String departureAirport;

    //@NotBlank(message = "Arrival airport code is required")
    //@Pattern(regexp = "^[A-Z]{3}$", message = "Arrival airport code must be 3 uppercase letters")
    private String arrivalAirport;

    //@NotBlank(message = "Flight status is required")
    private String status;

    //@NotBlank(message = "Aircraft code is required")
    private String aircraftCode;

    private LocalDateTime actualDeparture;

    private LocalDateTime actualArrival;

    // Getters and Setters

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

    public LocalDateTime getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(LocalDateTime actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public LocalDateTime getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(LocalDateTime actualArrival) {
        this.actualArrival = actualArrival;
    }
}
