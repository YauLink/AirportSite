package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository for persisting and retrieving flight bookings.
 */
public interface IBookingRepository extends JpaRepository<Booking, String> {

    @Query("SELECT DISTINCT b FROM Booking b JOIN b.tickets t WHERE t.passengerId = :passengerId")
    Page<Booking> findAllByPassengerId(@Param("passengerId") String passengerId, Pageable pageable);

    @Query("SELECT DISTINCT b FROM Booking b JOIN b.tickets t " +
            "WHERE b.bookRef = :bookRef AND t.passengerId = :passengerId")
    Optional<Booking> findByBookRefAndPassengerId(@Param("bookRef") String bookRef,
                                                  @Param("passengerId") String passengerId);
}
