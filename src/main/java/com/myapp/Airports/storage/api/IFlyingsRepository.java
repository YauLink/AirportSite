package com.myapp.Airports.storage.api;

import com.myapp.Airports.model.Flying;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.List;

public interface IFlyingsRepository extends JpaRepository<Flying, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from Flying f where f.flightId = :flightId")
    java.util.Optional<Flying> findByIdForUpdate(@Param("flightId") Integer flightId);
    List<Flying> findAllByDepartureAirportAndArrivalAirport(
            String departureAirport,
            String arrivalAirport,
            Pageable pageable);

    long countByDepartureAirportAndArrivalAirport(
            String departureAirport,
            String arrivalAirport);

}
