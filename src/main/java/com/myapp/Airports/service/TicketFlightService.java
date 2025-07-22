package com.myapp.Airports.service;

import com.myapp.Airports.model.TicketFlight;
import com.myapp.Airports.model.TicketFlightId;
import com.myapp.Airports.storage.api.ITicketFlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketFlightService {

    private final ITicketFlightRepository repository;

    public TicketFlightService(ITicketFlightRepository repository) {
        this.repository = repository;
    }

    public List<TicketFlight> findAll() {
        return repository.findAll();
    }

    public TicketFlight findById(TicketFlightId id) {
        return repository.findById(id).orElseThrow();
    }

    public TicketFlight save(TicketFlight ticketFlight) {
        return repository.save(ticketFlight);
    }

    public void deleteById(TicketFlightId id) {
        repository.deleteById(id);
    }
}
