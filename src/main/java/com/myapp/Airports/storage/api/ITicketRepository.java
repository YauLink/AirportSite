package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for persisting and querying flight tickets.
 */
public interface ITicketRepository extends JpaRepository<Ticket, String> {
    @EntityGraph(attributePaths = "booking")
    @Query("SELECT t FROM Ticket t")
    List<Ticket> findAllWithBooking();

    @EntityGraph(attributePaths = "booking")
    @Query("SELECT t FROM Ticket t WHERE t.ticketNo = :ticketNo")
    Optional<Ticket> findByIdWithBooking(@Param("ticketNo") String ticketNo);

    @Query("SELECT t FROM Ticket t " +
            "JOIN TicketFlight tf ON t = tf.ticket " +
            "WHERE tf.flight.flightId = :flightId")
    List<Ticket> findTicketsByFlightId(@Param("flightId") Integer flightId);

    @EntityGraph(attributePaths = "booking")
    @Query(value = "SELECT * FROM tickets ORDER BY random()", nativeQuery = true)
    Page<Ticket> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "booking")
    List<Ticket> findAllByPassengerId(String passengerId);
}
