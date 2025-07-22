package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.TicketFlight;
import com.myapp.Airports.model.TicketFlightId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITicketFlightRepository extends JpaRepository<TicketFlight, TicketFlightId> {
}
