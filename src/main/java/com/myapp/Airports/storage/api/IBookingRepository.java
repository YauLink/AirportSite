package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for persisting and retrieving flight bookings.
 */
public interface IBookingRepository extends JpaRepository<Booking, String> {
}
