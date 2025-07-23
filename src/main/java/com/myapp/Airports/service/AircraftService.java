package com.myapp.Airports.service;

import com.myapp.Airports.model.Aircraft;
import com.myapp.Airports.storage.api.IAircraftRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AircraftService {

    private final IAircraftRepository repository;

    public AircraftService(IAircraftRepository repository) {
        this.repository = repository;
    }

    public List<Aircraft> getAll() {
        return repository.findAll();
    }

    public Aircraft getById(String id) {
        return repository.findById(id).orElseThrow();
    }

    public Aircraft save(Aircraft aircraft) {
        return repository.save(aircraft);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}