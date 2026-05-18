package com.myapp.Airports.service;

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

        try {
            PageRequest req = PageRequest.of(
                    page,
                    size,
                    Sort.by("bookDate").descending()
            );

            System.out.println("⏳ Fetching paginated bookings from DB...");

            return bookingRepository.findAll(req);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch bookings", e);
        }
    }

    @Cacheable(value = "booking", key = "#bookRef")
    public Booking findByBookRef(String bookRef) {

        try {
            System.out.println("⏳ Fetching booking " + bookRef + " from DB...");

            return bookingRepository.findById(bookRef)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Booking not found with ref: " + bookRef
                            ));

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch booking: " + bookRef, e);
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
            bookingRepository.deleteById(bookRef);

        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel booking: " + bookRef, e);
        }
    }

    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public Booking save(Booking booking) {

        try {
            return bookingRepository.save(booking);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save booking", e);
        }
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

        try {
            return ticketFlightRepository.findBookingsByFlight(flightId);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to fetch bookings for flight: " + flightId,
                    e
            );
        }
    }
}