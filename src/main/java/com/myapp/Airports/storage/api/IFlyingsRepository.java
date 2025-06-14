package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Flying;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFlyingsRepository extends JpaRepository<Flying, Integer> {
    long countByArrivalAirportOrDepartureAirport(String airportOut, String airportIn);
    List<Flying> findAllByArrivalAirportOrDepartureAirport(String airportOut, String airportIn, Pageable pageable);

}
