package com.myapp.Airports.service;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.TicketFlight;
import com.myapp.Airports.storage.api.IBookingRepository;
import com.myapp.Airports.storage.api.ITicketFlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final IBookingRepository bookingRepository;
    private final ITicketFlightRepository ticketFlightRepository;

    public BookingService(IBookingRepository bookingRepository, ITicketFlightRepository ticketFlightRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketFlightRepository = ticketFlightRepository;
    }

    public List<Booking> findAll () {
        return bookingRepository.findAll();
    }

    public Booking findByBookRef(String bookRef) {
        return bookingRepository.findById(bookRef)
                .orElseThrow(() -> new RuntimeException("Booking not found with ref: " + bookRef));
    }

    public void updateBooking(String bookRef, Booking updatedBooking) {
        Booking existing = findByBookRef(bookRef);
        existing.setBookDate(updatedBooking.getBookDate());
        existing.setTotalAmount(updatedBooking.getTotalAmount());
        bookingRepository.save(existing);
    }

    public void cancelBooking(String bookRef) {
        bookingRepository.deleteById(bookRef);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void delete (String bookRef) {
        bookingRepository.deleteById(bookRef);
    }

    public Booking findById(String bookRef) {
        return bookingRepository.findById(bookRef).orElseThrow(()->new RuntimeException("Booking not found"));
    }

    public void assignSeat(String bookRef, String seatNo) {
        List<TicketFlight> ticketFlights = ticketFlightRepository.findByBookingRef(bookRef);
        for (TicketFlight tf : ticketFlights) {
            tf.setSeatNo(seatNo); // assuming TicketFlight has seatNo
        }
        ticketFlightRepository.saveAll(ticketFlights);
    }

    public List<Booking> findByFlightId(String flightId) {
        return ticketFlightRepository.findBookingsByFlight(flightId);
    }
}
