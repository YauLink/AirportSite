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

@Service
public class TicketService {

    private final ITicketRepository ticketRepo;
    private final IBookingRepository bookingRepo;

    public TicketService(ITicketRepository ticketRepo, IBookingRepository bookingRepo) {
        this.ticketRepo = ticketRepo;
        this.bookingRepo = bookingRepo;
    }

    @Cacheable(value = "tickets")
    public List<Ticket> findAll() {
        System.out.println("⏳ Fetching all tickets from DB...");
        return ticketRepo.findAll();
    }

    public Page<Ticket> getAllTickets(int n, int page) {
        return ticketRepo.findAll(PageRequest.of(page, n));
    }

    @Cacheable(value = "ticket", key = "#ticketNo")
    public Ticket findById(String ticketNo) {
        System.out.println("⏳ Fetching ticket " + ticketNo + " from DB...");
        return ticketRepo.findById(ticketNo)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @CacheEvict(value = {"tickets", "ticket"}, allEntries = true)
    public Ticket save(Ticket ticket) {
        return ticketRepo.save(ticket);
    }

    @CacheEvict(value = {"tickets", "ticket"}, allEntries = true)
    public void delete(String ticketNo) {
        ticketRepo.deleteById(ticketNo);
    }

    @Cacheable(value = "booking", key = "#bookRef")
    public Booking getBooking(String bookRef) {
        System.out.println("⏳ Fetching booking " + bookRef + " from DB...");
        return bookingRepo.findById(bookRef)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
}
