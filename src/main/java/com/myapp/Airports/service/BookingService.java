package com.myapp.Airports.service;

import com.myapp.Airports.exceptions.BookingNotFoundException;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.TicketFlight;
import com.myapp.Airports.model.BoardingPass;
import com.myapp.Airports.model.BoardingPassId;
import com.myapp.Airports.storage.api.IBookingRepository;
import com.myapp.Airports.storage.api.IBoardingPassRepository;
import com.myapp.Airports.storage.api.ITicketFlightRepository;
import com.myapp.Airports.storage.api.IFlyingsRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Handles booking operations for flights.
 */
@Service
public class BookingService {

    private final IBookingRepository bookingRepository;
    private final ITicketFlightRepository ticketFlightRepository;
    private final IBoardingPassRepository boardingPassRepository;
    private final IFlyingsRepository flyingRepository;
    private final BoardingPassService boardingPassService;

    public BookingService(
            IBookingRepository bookingRepository,
            ITicketFlightRepository ticketFlightRepository,
            IBoardingPassRepository boardingPassRepository,
            IFlyingsRepository flyingRepository,
            BoardingPassService boardingPassService) {

        this.bookingRepository = bookingRepository;
        this.ticketFlightRepository = ticketFlightRepository;
        this.boardingPassRepository = boardingPassRepository;
        this.flyingRepository = flyingRepository;
        this.boardingPassService = boardingPassService;
    }

    @Cacheable(value = "bookings")
    public Page<Booking> findAll(int page, int size) {

        PageRequest req = PageRequest.of(
                page,
                size,
                Sort.by("bookDate").descending());

        return bookingRepository.findAll(req);
    }

    @Cacheable(value = "booking", key = "#bookRef")
    public Booking findByBookRef(String bookRef) {

        return bookingRepository.findById(bookRef)
                .orElseThrow(() -> new BookingNotFoundException(bookRef));
    }

    @CacheEvict(
            value = {"bookings", "booking"},
            allEntries = true)
    public void updateBooking(
            String bookRef,
            Booking updatedBooking) {

        performUpdate(bookRef, updatedBooking);
    }

    @CacheEvict(
            value = {"bookings", "booking"},
            allEntries = true)
    public Booking updateBookingAndReturn(
            String bookRef,
            Booking updatedBooking) {

        return performUpdate(bookRef, updatedBooking);
    }

    private Booking performUpdate(
            String bookRef,
            Booking updatedBooking) {

        Booking existing = findByBookRef(bookRef);

        existing.setBookDate(updatedBooking.getBookDate());
        existing.setTotalAmount(updatedBooking.getTotalAmount());

        return bookingRepository.save(existing);
    }

    @CacheEvict(
            value = {"bookings", "booking"},
            allEntries = true)
    public void cancelBooking(String bookRef) {

        Booking booking = findByBookRef(bookRef);

        bookingRepository.delete(booking);
    }

    @CacheEvict(
            value = {"bookings", "booking"},
            allEntries = true)
    public Booking save(Booking booking) {

        return bookingRepository.save(booking);
    }

    @CacheEvict(
            value = {"bookings", "booking"},
            allEntries = true)
    public void delete(String bookRef) {

        Booking booking = findByBookRef(bookRef);

        bookingRepository.delete(booking);
    }

    @CacheEvict(
            value = {"bookings", "booking"},
            allEntries = true)
    @Transactional
    public void assignSeat(
            String bookRef,
            String seatNo) {

        List<TicketFlight> ticketFlights = ticketFlightRepository.findByBookingRef(bookRef);

        if (ticketFlights.isEmpty()) {
            throw new BookingNotFoundException(bookRef);
        }

        ticketFlights.sort(java.util.Comparator.comparing(TicketFlight::getFlightId));

        for (TicketFlight ticketFlight : ticketFlights) {
            Integer flightId = ticketFlight.getFlightId();
            // Serialize all seat allocations for this flight. This also makes
            // max(boarding_no)+1 safe; the database unique constraints remain
            // the final protection against races or legacy callers.
            flyingRepository.findByIdForUpdate(flightId)
                    .orElseThrow(() -> new BookingNotFoundException(bookRef));

            BoardingPassId passId = new BoardingPassId();
            passId.setTicketNo(ticketFlight.getTicketNo());
            passId.setFlightId(flightId);

            BoardingPass pass = boardingPassRepository.findById(passId)
                    .orElseGet(() -> {
                        BoardingPass created = new BoardingPass();
                        created.setId(passId);
                        created.setBoardingNo(boardingPassRepository
                                .findMaxBoardingNoByFlightId(flightId) + 1);
                        return created;
                    });
            pass.setSeatNo(seatNo);
            boardingPassService.create(pass);
            ticketFlight.setSeatNo(seatNo);
        }

        ticketFlightRepository.saveAll(ticketFlights);
    }

    public List<Booking> findByFlightId(Integer flightId) {

        return ticketFlightRepository.findBookingsByFlight(flightId);
    }
}
