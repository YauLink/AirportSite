package com.myapp.Airports.service;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.storage.api.IBookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final IBookingRepository repository;

    public BookingService(IBookingRepository repository) {
        this.repository = repository;
    }

    public List<Booking> findAll () {
        return repository.findAll();
    }

    public Booking save(Booking booking) {
        return repository.save(booking);
    }

    public void delete (String bookRef) {
        repository.deleteById(bookRef);
    }

    public Booking findById(String bookRef) {
        return repository.findById(bookRef).orElseThrow(()->new RuntimeException("Booking not found"));
    }
}
