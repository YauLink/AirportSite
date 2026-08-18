package com.myapp.Airports.controller.rest;

import com.myapp.Airports.controller.rest.RestFlightBookingController;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.FlyingService;
import com.myapp.Airports.service.TicketBookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestFlightBookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestFlightBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private TicketBookingService ticketBookingService;

    @MockBean
    private FlyingService flyingService;

    @Test
    void shouldConfirmBooking() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("USER_ID", "P001");
        session.setAttribute("USER_NAME", "John Smith");

        Flying flight = new Flying();
        flight.setFlightId(1);
        flight.setFlightNo("AA101");

        when(flyingService.findAllByIds(List.of(1)))
                .thenReturn(List.of(flight));

        mockMvc.perform(post("/api/user/confirm")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passengerName").value("John Smith"))
                .andExpect(jsonPath("$.flights[0].flightId").value(1));

        verify(flyingService).findAllByIds(List.of(1));
    }

    @Test
    void shouldReturnUnauthorizedWhenConfirmWithoutLogin() throws Exception {

        mockMvc.perform(post("/api/user/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1]"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error")
                        .value("User not authenticated"));

        verifyNoInteractions(flyingService);
    }

    @Test
    void shouldBookFlights() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("USER_ID", "P001");
        session.setAttribute("USER_NAME", "John Smith");
        session.setAttribute("SELECTED_FLIGHTS", List.of(1));

        Flying flight = new Flying();
        flight.setFlightId(1);

        when(flyingService.findAllByIds(List.of(1)))
                .thenReturn(List.of(flight));

        mockMvc.perform(post("/api/user/book")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Booking created successfully"))
                .andExpect(jsonPath("$.bookingRef").exists())
                .andExpect(jsonPath("$.total").value(100));

        verify(ticketBookingService)
                .createBookingWithTickets(
                        any(Booking.class),
                        eq("P001"),
                        eq("John Smith"),
                        eq("{}"),
                        eq(List.of(1)),
                        eq(List.of("Economy")),
                        anyList());
    }

    @Test
    void shouldReturnUnauthorizedWhenBookingWithoutLogin() throws Exception {

        mockMvc.perform(post("/api/user/book"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error")
                        .value("User not authenticated"));

        verifyNoInteractions(bookingService);
    }

    @Test
    void shouldReturnBadRequestWhenNoFlightsSelected() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("USER_ID", "P001");
        session.setAttribute("USER_NAME", "John Smith");

        mockMvc.perform(post("/api/user/book")
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("No flights selected"));

        verifyNoInteractions(bookingService);
    }
}
