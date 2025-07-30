package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Seat;
import com.myapp.Airports.model.SeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISeatRepository extends JpaRepository<Seat, SeatId> {

    @Query(value = """
    SELECT s.*
    FROM bookings.seats s
    JOIN bookings.flights f ON s.aircraft_code = f.aircraft_code
    WHERE f.flight_id = :flightId
      AND NOT EXISTS (
          SELECT 1
          FROM bookings.ticket_flights tf
          WHERE tf.flight_id = f.flight_id
            AND tf.seat_no = s.seat_no
      )
    """, nativeQuery = true)
    List<Seat> findAvailableSeatsByFlightId(@Param("flightId") Integer flightId);
}
