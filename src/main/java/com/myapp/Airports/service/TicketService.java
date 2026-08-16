package com.myapp.Airports.service;

import com.myapp.Airports.exceptions.BookingNotFoundException;
import com.myapp.Airports.exceptions.TicketNotFoundException;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.storage.api.IBookingRepository;
import com.myapp.Airports.storage.api.ITicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles ticket operations for flights.
 */
@Service
public class TicketService {

    private final ITicketRepository ticketRepo;
    private final IBookingRepository bookingRepo;

    public TicketService(
            ITicketRepository ticketRepo,
            IBookingRepository bookingRepo) {

        this.ticketRepo = ticketRepo;
        this.bookingRepo = bookingRepo;
    }

    @Cacheable(value = "tickets")
    public List<Ticket> findAll() {
        return ticketRepo.findAll();
    }

    @Cacheable(value = "tickets")
    public Page<Ticket> getAllTickets(int n, int page) {
        return ticketRepo.findAll(
                PageRequest.of(page, n)
        );
    }

    @Cacheable(value = "ticket", key = "#ticketNo")
    public Ticket findById(String ticketNo) {

        return ticketRepo.findById(ticketNo)
                .orElseThrow(() -> new TicketNotFoundException(ticketNo));
    }

    @Cacheable(value = "tickets", key = "#passengerId")
    public List<Ticket> findAllByUserId(String passengerId) {
        return ticketRepo.findAllByPassengerId(passengerId);
    }

    @CacheEvict(
            value = {"tickets", "ticket"},
            allEntries = true
    )
    public Ticket save(Ticket ticket) {
        return ticketRepo.save(ticket);
    }

    @CacheEvict(
            value = {"tickets", "ticket"},
            allEntries = true
    )
    public void delete(String ticketNo) {

        Ticket ticket = findById(ticketNo);

        ticketRepo.delete(ticket);
    }

    @Cacheable(value = "booking", key = "#bookRef")
    public Booking getBooking(String bookRef) {

        return bookingRepo.findById(bookRef)
                .orElseThrow(() -> new BookingNotFoundException(bookRef));
    }
}