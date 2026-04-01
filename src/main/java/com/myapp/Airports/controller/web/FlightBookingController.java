package com.myapp.Airports.controller.web;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.service.BookingService;
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

    public FlightBookingController(BookingService bookingService,
                                   TicketBookingService ticketBookingService) {
        this.bookingService = bookingService;
        this.ticketBookingService = ticketBookingService;
    }

    @PostMapping("/book")
    public String bookFlights(
            HttpSession session,
            @RequestParam("flightIds") List<Integer> flightIds,
            @RequestParam("fares") List<String> fares,
            @RequestParam("amounts") List<BigDecimal> amounts,
            @RequestParam("contactData") String contactData,
            Model model) {

        String passengerId = (String) session.getAttribute("USER_ID");
        String passengerName = (String) session.getAttribute("USER_NAME");

        if (passengerId == null || passengerName == null) {
            return "redirect:/user/login";
        }

        Booking booking = new Booking();
        booking.setBookRef("BKG-" + System.currentTimeMillis()); // simple unique ref
        booking.setBookDate(LocalDateTime.now());
        booking.setTotalAmount(amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
        bookingService.save(booking);

        ticketBookingService.createTicketsForBooking(
                booking,
                passengerId,
                passengerName,
                contactData,
                flightIds,
                fares,
                amounts
        );

        return "redirect:/user/cabinet";
    }
}