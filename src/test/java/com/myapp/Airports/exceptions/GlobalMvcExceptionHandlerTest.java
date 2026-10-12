package com.myapp.Airports.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalMvcExceptionHandlerTest {

    private final GlobalMvcExceptionHandler handler = new GlobalMvcExceptionHandler();

    @Test
    void notFoundUsesExisting404ViewAndMessage() {
        Model model = new ExtendedModelMap();

        String view = handler.handleBookingNotFound(new BookingNotFoundException("ABC123"), model);

        assertEquals("error/404", view);
        assertEquals("Booking Not Found", model.getAttribute("errorTitle"));
        assertEquals("Booking with reference 'ABC123' was not found.", model.getAttribute("errorMessage"));
    }

    @Test
    void malformedMvcRequestUses400View() {
        Model model = new ExtendedModelMap();

        String view = handler.handleMalformedRequest(
                new org.springframework.web.bind.MissingServletRequestParameterException("seatNo", String.class),
                model);

        assertEquals("error/400", view);
        assertEquals("Invalid Request", model.getAttribute("errorTitle"));
    }
}