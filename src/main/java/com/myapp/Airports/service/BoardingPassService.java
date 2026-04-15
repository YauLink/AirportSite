package com.myapp.Airports.service;

import com.myapp.Airports.model.BoardingPass;
import com.myapp.Airports.storage.api.IBoardingPassRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

public class BoardingPassService {

    private final IBoardingPassRepository repository;

    public BoardingPassService(IBoardingPassRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BoardingPass create(BoardingPass bp) {

        try {
            return repository.save(bp);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Seat already taken for this flight");
        }
    }
}
