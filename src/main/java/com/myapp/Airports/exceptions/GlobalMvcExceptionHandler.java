package com.myapp.Airports.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalMvcExceptionHandler {

    @ExceptionHandler(FlightNotFoundException.class)
    public String handleFlightNotFound(
            FlightNotFoundException ex,
            Model model) {

        model.addAttribute("errorTitle", "Flight Not Found");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/404";
    }

    @ExceptionHandler(AirportNotFoundException.class)
    public String handleAirportNotFound(
            AirportNotFoundException ex,
            Model model) {

        model.addAttribute("errorTitle", "Airport Not Found");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/404";
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public String handleBookingNotFound(
            BookingNotFoundException ex,
            Model model) {

        model.addAttribute("errorTitle", "Booking Not Found");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/404";
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public String handleTicketNotFound(
            TicketNotFoundException ex,
            Model model) {

        model.addAttribute("errorTitle", "Ticket Not Found");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleAnyException(
            Exception ex,
            Model model) {

        model.addAttribute("errorTitle", "Unexpected Error");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/500";
    }
}