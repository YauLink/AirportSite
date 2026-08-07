package com.myapp.Airports.service;

import com.myapp.Airports.exceptions.BookingNotFoundException;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.TicketFlight;
import com.myapp.Airports.storage.api.IBookingRepository;
import com.myapp.Airports.storage.api.ITicketFlightRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles booking operations for flights.
 */
@Service
public class BookingService {

    private final IBookingRepository bookingRepository;
    private final ITicketFlightRepository ticketFlightRepository;

    public BookingService(IBookingRepository bookingRepository,
                          ITicketFlightRepository ticketFlightRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketFlightRepository = ticketFlightRepository;
    }

    @Cacheable(value = "bookings")
    public Page<Booking> findAll(int page, int size) {

        PageRequest req = PageRequest.of(
                page,
                size,
                Sort.by("bookDate").descending()
        );

        return bookingRepository.findAll(req);
    }

    @Cacheable(value = "booking", key = "#bookRef")
    public Booking findByBookRef(String bookRef) {

        try {
            System.out.println("⏳ Fetching booking " + bookRef + " from DB...");

            return bookingRepository.findById(bookRef)
                    .orElseThrow(() ->
                            new BookingNotFoundException(bookRef));

        } catch (Exception e) {
            throw new BookingNotFoundException(bookRef);
        }
    }

    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public void updateBooking(String bookRef, Booking updatedBooking) {

        try {
            performUpdate(bookRef, updatedBooking);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update booking: " + bookRef, e);
        }
    }

    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public Booking updateBookingAndReturn(String bookRef,
                                          Booking updatedBooking) {

        try {
            return performUpdate(bookRef, updatedBooking);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to update and return booking: " + bookRef,
                    e
            );
        }
    }

    private Booking performUpdate(String bookRef,
                                  Booking updatedBooking) {

        Booking existing = findByBookRef(bookRef);

        existing.setBookDate(updatedBooking.getBookDate());
        existing.setTotalAmount(updatedBooking.getTotalAmount());

        return bookingRepository.save(existing);
    }

    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public void cancelBooking(String bookRef) {

        try {
            Booking booking = findByBookRef(bookRef);

            bookingRepository.delete(booking);

        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel booking: " + bookRef, e);
        }
    }

    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public Booking save(Booking booking) {

        return bookingRepository.save(booking);
    }

    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public void delete(String bookRef) {

        try {
            bookingRepository.deleteById(bookRef);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete booking: " + bookRef, e);
        }
    }

    public void assignSeat(String bookRef, String seatNo) {

        try {
            List<TicketFlight> ticketFlights =
                    ticketFlightRepository.findByBookingRef(bookRef);

            if (ticketFlights.isEmpty()) {
                throw new BookingNotFoundException(bookRef);
            }

            for (TicketFlight tf : ticketFlights) {
                tf.setSeatNo(seatNo);
            }

            ticketFlightRepository.saveAll(ticketFlights);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to assign seat for booking: " + bookRef,
                    e
            );
        }
    }

    public List<Booking> findByFlightId(Integer flightId) {

        return ticketFlightRepository.findBookingsByFlight(flightId);
    }
}