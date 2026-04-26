package com.myapp.Airports.controller.web;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.FlyingService;
import com.myapp.Airports.service.TicketBookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/user")
public class FlightBookingController {

    private final BookingService bookingService;
    private final TicketBookingService ticketBookingService;
    private final FlyingService flyingService;

    public FlightBookingController(BookingService bookingService,
                                   TicketBookingService ticketBookingService,
                                   FlyingService flyingService) {
        this.bookingService = bookingService;
        this.ticketBookingService = ticketBookingService;
        this.flyingService = flyingService;
    }

    /**
     * STEP 1: go to confirm page
     */
    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam("flightIds") List<Integer> flightIds,
                                 HttpSession session,
                                 Model model) {

        String passengerId = (String) session.getAttribute("USER_ID");
        String passengerName = (String) session.getAttribute("USER_NAME");

        if (passengerId == null || passengerName == null) {
            return "redirect:/user/login";
        }

        List<Flying> flights = flyingService.findAllByIds(flightIds);

        model.addAttribute("flights", flights);
        model.addAttribute("passengerName", passengerName);

        // store for final step
        session.setAttribute("SELECTED_FLIGHTS", flightIds);

        return "user/booking_form";
    }

    /**
     * STEP 2: final booking
     */
    @PostMapping("/book")
    public String bookFlights(HttpSession session) {

        String passengerId = (String) session.getAttribute("USER_ID");
        String passengerName = (String) session.getAttribute("USER_NAME");

        if (passengerId == null || passengerName == null) {
            return "redirect:/user/login";
        }

        List<Integer> flightIds =
                (List<Integer>) session.getAttribute("SELECTED_FLIGHTS");

        if (flightIds == null || flightIds.isEmpty()) {
            return "redirect:/filters";
        }

        List<Flying> flights = flyingService.findAllByIds(flightIds);

        // simple price calculation (replace later)
        BigDecimal total = flights.stream()
                .map(f -> BigDecimal.valueOf(100)) // placeholder price
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Booking booking = new Booking();
        booking.setBookRef("BKG-" + System.currentTimeMillis());
        booking.setBookDate(LocalDateTime.now());
        booking.setTotalAmount(total);

        bookingService.save(booking);

        ticketBookingService.createTicketsForBooking(
                booking,
                passengerId,
                passengerName,
                "{}",
                flightIds,
                List.of("Economy"),
                List.of(total)
        );

        // cleanup session
        session.removeAttribute("SELECTED_FLIGHTS");

        return "redirect:/user/cabinet";
    }
}