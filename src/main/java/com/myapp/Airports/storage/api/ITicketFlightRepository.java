package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.TicketFlight;
import com.myapp.Airports.model.TicketFlightId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITicketFlightRepository extends JpaRepository<TicketFlight, TicketFlightId> {

    @Query("SELECT DISTINCT t.booking " +
            "FROM TicketFlight tf " +
            "JOIN tf.ticket t " +
            "WHERE tf.flight.flightId = :flightId")
    List<Booking> findBookingsByFlight(@Param("flightId") Integer flightId);

    @Query("SELECT tf FROM TicketFlight tf WHERE tf.ticket.booking.bookRef = :bookRef")
    List<TicketFlight> findByBookingRef(@Param("bookRef") String bookRef);
}
