package com.myapp.Airports.service;

import com.myapp.Airports.model.Seat;
import com.myapp.Airports.model.SeatId;
import com.myapp.Airports.storage.api.ISeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final ISeatRepository repository;

    public SeatService(ISeatRepository repository) {
        this.repository = repository;
    }

    public List<Seat> getAll() {
        return repository.findAll();
    }

    public Seat getById(SeatId id) {
        return repository.findById(id).orElseThrow();
    }

    public Seat save(Seat seat) {
        return repository.save(seat);
    }

    public void delete(SeatId id) {
        repository.deleteById(id);
    }
}
