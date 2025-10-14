package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITicketRepository extends JpaRepository<Ticket, String> {
    @Query("SELECT t FROM Ticket t " +
            "JOIN TicketFlight tf ON t = tf.ticket " +
            "WHERE tf.flight.flightId = :flightId")
    List<Ticket> findTicketsByFlightId(@Param("flightId") Integer flightId);

    @Query(value = "SELECT * FROM tickets ORDER BY random()", nativeQuery = true)
    Page<Ticket> findAll(Pageable pageable);
}
