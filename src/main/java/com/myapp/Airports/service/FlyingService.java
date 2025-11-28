package com.myapp.Airports.service;

import com.myapp.Airports.model.Flying;
import com.myapp.Airports.storage.api.IFlyingsRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Cacheable(value = "flight", key = "#flight_id")
    public Optional<Flying> findById(Integer id) {
        System.out.println("⏳ Fetching flight " + id + " from DB...");
        return flyingRepository.findById(id);
    }

    @CacheEvict(value = {"flights", "flight"}, allEntries = true)
    public void deleteById(Integer id) {
        flyingRepository.deleteById(id);
    }
}

