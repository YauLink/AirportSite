package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Flying;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFlyingsRepository extends JpaRepository<Flying, Integer> {
    List<Flying> findAllByDepartureAirportAndArrivalAirport(
            String departureAirport,
            String arrivalAirport,
            Pageable pageable);

    long countByDepartureAirportAndArrivalAirport(
            String departureAirport,
            String arrivalAirport);

}
