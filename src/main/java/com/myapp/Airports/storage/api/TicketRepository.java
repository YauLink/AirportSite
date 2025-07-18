package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String> {
}
