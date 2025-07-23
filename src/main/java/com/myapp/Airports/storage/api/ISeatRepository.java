package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Seat;
import com.myapp.Airports.model.SeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISeatRepository extends JpaRepository<Seat, SeatId> {
}
