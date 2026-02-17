package com.myapp.Airports.service;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Seat;
import com.myapp.Airports.model.SeatId;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.storage.api.IFlyingsRepository;
import com.myapp.Airports.storage.api.ISeatRepository;
import com.myapp.Airports.storage.api.ITicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles seat operations for flights.
 */
@Service
public class SeatService {

    private final ISeatRepository seatRepository;

    public SeatService(ISeatRepository repository) {
        this.seatRepository = repository;
    }

    public List<Seat> getAll() {
        return seatRepository.findAll();
    }

    public Seat getById(SeatId id) {
        return seatRepository.findById(id).orElseThrow();
    }

    public Seat save(Seat seat) {
        return seatRepository.save(seat);
    }

    public void delete(SeatId id) {
        seatRepository.deleteById(id);
    }

    public List<Seat> findAvailableForFlight(Integer flightId){
        return seatRepository.findAvailableSeatsByFlightId(flightId);
    }
}
