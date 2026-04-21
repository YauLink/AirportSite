package com.myapp.Airports.controller.web;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.TicketBookingService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightBookingController.class)
class FlightBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private TicketBookingService ticketBookingService;


    @Test
    void shouldRedirectToLoginWhenUserNotInSession() throws Exception {

        mockMvc.perform(post("/user/book")
                        .with(csrf())
                        .with(user("test"))
                        .param("flightIds", "1")
                        .param("fares", "Economy")
                        .param("amounts", "100.00")
                        .param("contactData", "{}"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));

        verifyNoInteractions(bookingService);
        verifyNoInteractions(ticketBookingService);
    }

    @Test
    void shouldCreateBookingAndRedirectToCabinet() throws Exception {

        mockMvc.perform(post("/user/book")
                        .with(csrf())
                        .with(user("test"))
                        .sessionAttr("USER_ID", "123")
                        .sessionAttr("USER_NAME", "John Doe")
                        .param("flightIds", "1", "2")
                        .param("fares", "Economy", "Business")
                        .param("amounts", "100.00", "200.00")
                        .param("contactData", "{\"email\":\"test@test.com\"}"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/cabinet"));

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingService, times(1)).save(bookingCaptor.capture());

        Booking savedBooking = bookingCaptor.getValue();

        assert savedBooking.getTotalAmount().compareTo(new BigDecimal("300.00")) == 0;

        verify(ticketBookingService, times(1)).createTicketsForBooking(
                any(Booking.class),
                eq("123"),
                eq("John Doe"),
                eq("{\"email\":\"test@test.com\"}"),
                eq(List.of(1, 2)),
                eq(List.of("Economy", "Business")),
                eq(List.of(new BigDecimal("100.00"), new BigDecimal("200.00")))
        );
    }
}