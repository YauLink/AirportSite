package com.myapp.Airports.controller.rest;

import com.myapp.Airports.exceptions.InvalidBookingStateException;
import com.myapp.Airports.exceptions.UserNotAuthenticatedException;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.FlyingService;
import com.myapp.Airports.service.TicketBookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST controller responsible for flight booking operations.
 * <p>
 * Provides endpoints for confirming selected flights
 * and creating bookings for authenticated users.
 * </p>
 */
@RestController
@RequestMapping("/api/user")
public class RestFlightBookingController {

    private final BookingService bookingService;
    private final TicketBookingService ticketBookingService;
    private final FlyingService flyingService;

    public RestFlightBookingController(
            BookingService bookingService,
            TicketBookingService ticketBookingService,
            FlyingService flyingService) {

        this.bookingService = bookingService;
        this.ticketBookingService = ticketBookingService;
        this.flyingService = flyingService;
    }

    /**
     * STEP 1: confirm selected flights.
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmBooking(
            @RequestBody List<Integer> flightIds,
            HttpSession session) {

        String passengerId = (String) session.getAttribute("USER_ID");
        String passengerName = (String) session.getAttribute("USER_NAME");

        if (passengerId == null || passengerName == null) {
            throw new UserNotAuthenticatedException("User not authenticated");
        }

        List<Flying> flights = flyingService.findAllByIds(flightIds);

        session.setAttribute("SELECTED_FLIGHTS", flightIds);

        return ResponseEntity.ok(
                Map.of(
                        "passengerName", passengerName,
                        "flights", flights
                )
        );
    }

    /**
     * STEP 2: create booking.
     */
    @PostMapping("/book")
    public ResponseEntity<?> bookFlights(HttpSession session) {

        String passengerId = (String) session.getAttribute("USER_ID");
        String passengerName = (String) session.getAttribute("USER_NAME");

        if (passengerId == null || passengerName == null) {
            throw new UserNotAuthenticatedException("User not authenticated");
        }

        @SuppressWarnings("unchecked")
        List<Integer> flightIds =
                (List<Integer>) session.getAttribute(
                        "SELECTED_FLIGHTS"
                );

        if (flightIds == null || flightIds.isEmpty()) {
            throw new InvalidBookingStateException("No flights selected");
        }

        List<Flying> flights = flyingService.findAllByIds(flightIds);

        BigDecimal total = flights.stream()
                .map(f -> BigDecimal.valueOf(100))
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        Booking booking = new Booking();
        booking.setBookRef(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        booking.setBookDate(LocalDateTime.now());
        booking.setTotalAmount(total);

        List<BigDecimal> amounts = flights.stream()
                .map(flight -> BigDecimal.valueOf(100))
                .toList();
        List<String> fares = flights.stream()
                .map(flight -> "Economy")
                .toList();

        ticketBookingService.createBookingWithTickets(
                booking,
                passengerId,
                passengerName,
                "{}",
                flightIds,
                fares,
                amounts
        );

        session.removeAttribute("SELECTED_FLIGHTS");

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Booking created successfully",
                        "bookingRef",
                        booking.getBookRef(),
                        "total", total)
        );
    }
}
