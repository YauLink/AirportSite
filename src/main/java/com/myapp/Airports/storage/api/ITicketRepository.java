package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findTicketsByFlightId(String aircraftCode);
}
