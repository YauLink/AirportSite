package com.myapp.Airports.service;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.storage.api.IBookingRepository;
import com.myapp.Airports.storage.api.ITicketRepository;
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

    public List<Ticket> findAll() {
        return ticketRepo.findAll();
    }

    public Ticket findById(String ticketNo) {
        return ticketRepo.findById(ticketNo)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Ticket save(Ticket ticket) {
        return ticketRepo.save(ticket);
    }

    public void delete(String ticketNo) {
        ticketRepo.deleteById(ticketNo);
    }

    public Booking getBooking(String bookRef) {
        return bookingRepo.findById(bookRef)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
}
