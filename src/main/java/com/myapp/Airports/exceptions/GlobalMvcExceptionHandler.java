package com.myapp.Airports.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.myapp.Airports.controller.web")
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

    @ExceptionHandler(SeatUnavailableException.class)
    public String handleSeatUnavailable(
            SeatUnavailableException ex,
            Model model) {

        model.addAttribute("errorTitle", "Seat Unavailable");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/409";
    }

    @ExceptionHandler({InvalidBookingStateException.class, IllegalArgumentException.class})
    public String handleBadRequest(
            RuntimeException ex,
            Model model) {

        model.addAttribute("errorTitle", "Invalid Request");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/400";
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public String handleUnauthenticated(
            UserNotAuthenticatedException ex,
            Model model) {

        model.addAttribute("errorTitle", "Authentication Required");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/401";
    }

    @ExceptionHandler({EntityAlreadyExistsException.class, UserNotFoundException.class})
    public String handleConflictOrUserNotFound(
            RuntimeException ex,
            Model model) {

        model.addAttribute("errorTitle", ex instanceof UserNotFoundException
                ? "User Not Found" : "Already Exists");
        model.addAttribute("errorMessage", ex.getMessage());

        return ex instanceof UserNotFoundException ? "error/404" : "error/409";
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
