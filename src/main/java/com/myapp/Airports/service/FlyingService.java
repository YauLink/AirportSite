package com.myapp.Airports.service;

import com.myapp.Airports.model.Flying;
import com.myapp.Airports.storage.api.IFlyingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlyingService {

    private final IFlyingsRepository flyingRepository;

    public FlyingService(IFlyingsRepository flyingRepository) {
        this.flyingRepository = flyingRepository;
    }

    public void save(Flying flying) {
        flyingRepository.save(flying);
    }

    public List<Flying> findAll() {
        return flyingRepository.findAll();
    }

    public Optional<Flying> findById(Integer id) {
        return flyingRepository.findById(id);
    }

    public void deleteById(Integer id) {
        flyingRepository.deleteById(id);
    }
}

