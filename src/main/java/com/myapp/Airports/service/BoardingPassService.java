package com.myapp.Airports.service;

import com.myapp.Airports.exceptions.SeatUnavailableException;
import com.myapp.Airports.model.BoardingPass;
import com.myapp.Airports.storage.api.IBoardingPassRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardingPassService {

    private final IBoardingPassRepository repository;

    public BoardingPassService(IBoardingPassRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BoardingPass create(BoardingPass bp) {

        try {
            // save() may defer the INSERT until transaction commit.  Flush here so
            // the database's unique (flight_id, seat_no) constraint is handled at
            // the allocation boundary, not after the caller has returned.
            return repository.saveAndFlush(bp);

        } catch (DataIntegrityViolationException e) {
            throw new SeatUnavailableException("Seat already taken for this flight");
        }
    }
}
