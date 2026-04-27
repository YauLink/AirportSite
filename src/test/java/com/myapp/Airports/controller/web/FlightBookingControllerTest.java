package com.myapp.Airports.controller.web;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.FlyingService;
import com.myapp.Airports.service.TicketBookingService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
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
@WithMockUser
class FlightBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private TicketBookingService ticketBookingService;

    @MockBean
    private FlyingService flyingService;


    @Test
    void shouldRedirectToLoginWhenUserNotInSession() throws Exception {
        mockMvc.perform(post("/user/book")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));

        verifyNoInteractions(bookingService);
    }

    @Test
    void shouldCreateBookingAndRedirectToCabinet() throws Exception {

        when(flyingService.findAllByIds(List.of(1, 2)))
                .thenReturn(List.of(new Flying(), new Flying()));

        mockMvc.perform(post("/user/book")
                        .with(csrf())
                        .sessionAttr("USER_ID", "123")
                        .sessionAttr("USER_NAME", "John Doe")
                        .sessionAttr("SELECTED_FLIGHTS", List.of(1, 2)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/cabinet"));

        verify(bookingService, times(1)).save(any(Booking.class));
        verify(ticketBookingService, times(1)).createTicketsForBooking(
                any(), eq("123"), eq("John Doe"), anyString(),
                eq(List.of(1, 2)), anyList(), anyList()
        );
    }
}