package com.myapp.Airports.service;

import com.myapp.Airports.dto.FlyingDTO;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.storage.api.IFlyingsRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Handles flights operations for flights.
 */
@Service
public class FlyingService {

    private final IFlyingsRepository flyingRepository;

    public FlyingService(IFlyingsRepository flyingRepository) {
        this.flyingRepository = flyingRepository;
    }

    @CacheEvict(value = {"flights"}, allEntries = true)
    public void save(Flying flying) {
        flyingRepository.save(flying);
    }

    @CacheEvict(value = {"flights"}, allEntries = true)
    public Flying saveAndReturn(Flying flying) {
        return flyingRepository.save(flying);
    }

    @Cacheable(value = "flights")
    public List<Flying> findAll() {
        System.out.println("⏳ Fetching all flights from DB...");
        return flyingRepository.findAll();
    }

    @Cacheable(value = "flight", key = "#id")
    public Optional<Flying> findById(Integer id) {
        System.out.println("⏳ Fetching flight " + id + " from DB...");
        return flyingRepository.findById(id);
    }

    @CacheEvict(value = {"flights", "flight"}, allEntries = true)
    public void deleteById(Integer id) {
        flyingRepository.deleteById(id);
    }

    public void update (Integer id, FlyingDTO dto) {

        Flying flight = flyingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        flight.setFlightNo(dto.getFlightNo());
        flight.setScheduledDeparture(dto.getScheduledDeparture());
        flight.setScheduledArrival(dto.getScheduledArrival());
        flight.setDepartureAirport(dto.getDepartureAirport());
        flight.setArrivalAirport(dto.getArrivalAirport());
        flight.setStatus(dto.getStatus());
        flight.setAircraftCode(dto.getAircraftCode());

        flyingRepository.save(flight);
    }
}

