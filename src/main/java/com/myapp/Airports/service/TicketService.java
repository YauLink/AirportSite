package com.myapp.Airports.service;

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

    public TicketService(ITicketRepository ticketRepo,
                         IBookingRepository bookingRepo) {
        this.ticketRepo = ticketRepo;
        this.bookingRepo = bookingRepo;
    }

    @Cacheable(value = "tickets")
    public List<Ticket> findAll() {

        try {
            System.out.println("⏳ Fetching all tickets from DB...");
            return ticketRepo.findAll();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch tickets", e);
        }
    }

    @Cacheable(value = "tickets")
    public Page<Ticket> getAllTickets(int n, int page) {

        try {
            return ticketRepo.findAll(PageRequest.of(page, n));

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch paginated tickets", e);
        }
    }

    @Cacheable(value = "ticket", key = "#ticketNo")
    public Ticket findById(String ticketNo) {

        try {
            System.out.println("⏳ Fetching ticket " + ticketNo + " from DB...");

            return ticketRepo.findById(ticketNo)
                    .orElseThrow(() ->
                            new RuntimeException("Ticket not found"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch ticket: " + ticketNo, e);
        }
    }

    @Cacheable(value = "tickets", key = "#passengerId")
    public List<Ticket> findAllByUserId(String passengerId) {

        try {
            System.out.println("⏳ Fetching tickets of User " + passengerId + " from DB...");

            return ticketRepo.findAllByPassengerId(passengerId);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to fetch tickets for passenger: " + passengerId,
                    e
            );
        }
    }

    @CacheEvict(value = {"tickets", "ticket"}, allEntries = true)
    public Ticket save(Ticket ticket) {

        try {
            return ticketRepo.save(ticket);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save ticket", e);
        }
    }

    @CacheEvict(value = {"tickets", "ticket"}, allEntries = true)
    public void delete(String ticketNo) {

        try {
            ticketRepo.deleteById(ticketNo);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete ticket: " + ticketNo, e);
        }
    }

    @Cacheable(value = "booking", key = "#bookRef")
    public Booking getBooking(String bookRef) {

        try {
            System.out.println("⏳ Fetching booking " + bookRef + " from DB...");

            return bookingRepo.findById(bookRef)
                    .orElseThrow(() ->
                            new RuntimeException("Booking not found"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch booking: " + bookRef, e);
        }
    }
}