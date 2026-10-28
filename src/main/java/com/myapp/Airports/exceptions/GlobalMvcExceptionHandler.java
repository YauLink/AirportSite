package com.myapp.Airports.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice(basePackages = "com.myapp.Airports.controller.web")
public class GlobalMvcExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FlightNotFoundException.class)
    public String handleFlightNotFound(
            FlightNotFoundException ex,
            Model model) {

        model.addAttribute("errorTitle", "Flight Not Found");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/404";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AirportNotFoundException.class)
    public String handleAirportNotFound(
            AirportNotFoundException ex,
            Model model) {

        model.addAttribute("errorTitle", "Airport Not Found");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/404";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BookingNotFoundException.class)
    public String handleBookingNotFound(
            BookingNotFoundException ex,
            Model model) {

        model.addAttribute("errorTitle", "Booking Not Found");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/404";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TicketNotFoundException.class)
    public String handleTicketNotFound(
            TicketNotFoundException ex,
            Model model) {

        model.addAttribute("errorTitle", "Ticket Not Found");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/404";
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SeatUnavailableException.class)
    public String handleSeatUnavailable(
            SeatUnavailableException ex,
            Model model) {

        model.addAttribute("errorTitle", "Seat Unavailable");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/409";
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMalformedRequest(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Invalid Request");
        model.addAttribute("errorMessage", "The request could not be understood.");
        return "error/400";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidBookingStateException.class, IllegalArgumentException.class})
    public String handleBadRequest(
            RuntimeException ex,
            Model model) {

        model.addAttribute("errorTitle", "Invalid Request");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/400";
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserNotAuthenticatedException.class)
    public String handleUnauthenticated(
            UserNotAuthenticatedException ex,
            Model model) {

        model.addAttribute("errorTitle", "Authentication Required");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/401";
    }

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(TooManyLoginAttemptsException.class)
    public String handleTooManyLoginAttempts(
            TooManyLoginAttemptsException ex,
            Model model) {
        model.addAttribute("errorTitle", "Too Many Login Attempts");
        model.addAttribute("errorMessage", ex.getMessage());
        return "user/login";
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public String handleAlreadyExists(EntityAlreadyExistsException ex, Model model) {
        model.addAttribute("errorTitle", "Already Exists");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/409";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "User Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleAnyException(
            Exception ex,
            Model model) {

        model.addAttribute("errorTitle", "Unexpected Error");
        model.addAttribute("errorMessage", ex.getMessage());

        return "error/500";
    }
}
