package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookingRepository extends JpaRepository<Booking, String> {
}
