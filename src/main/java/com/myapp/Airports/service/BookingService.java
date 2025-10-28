package com.myapp.Airports.service;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.TicketFlight;
import com.myapp.Airports.storage.api.IBookingRepository;
import com.myapp.Airports.storage.api.ITicketFlightRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "bookings")
    public List<Booking> findAll() {
        System.out.println("⏳ Fetching all bookings from DB...");
        return bookingRepository.findAll();
    }

    @Cacheable(value = "booking", key = "#book_ref")
    public Booking findByBookRef(String bookRef) {
        System.out.println("⏳ Fetching booking " + bookRef + " from DB...");
        return bookingRepository.findById(bookRef)
                .orElseThrow(() -> new RuntimeException("Booking not found with ref: " + bookRef));
    }

    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public void updateBooking(String bookRef, Booking updatedBooking) {
        Booking existing = findByBookRef(bookRef);
        existing.setBookDate(updatedBooking.getBookDate());
        existing.setTotalAmount(updatedBooking.getTotalAmount());
        bookingRepository.save(existing);
    }

    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public void cancelBooking(String bookRef) {
        bookingRepository.deleteById(bookRef);
    }

    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public void delete(String bookRef) {
        bookingRepository.deleteById(bookRef);
    }

    public void assignSeat(String bookRef, String seatNo) {
        List<TicketFlight> ticketFlights = ticketFlightRepository.findByBookingRef(bookRef);
        for (TicketFlight tf : ticketFlights) {
            tf.setSeatNo(seatNo);
        }
        ticketFlightRepository.saveAll(ticketFlights);
    }

    public List<Booking> findByFlightId(Integer flightId) {
        return ticketFlightRepository.findBookingsByFlight(flightId);
    }
}
