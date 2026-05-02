package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.model.TicketFlight;
import com.myapp.Airports.model.TicketFlightId;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@WithMockUser
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private TicketService ticketService;

    @Test
    void shouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    void shouldLoginAndRedirectToCabinet() throws Exception {
        AuthResponseDTO authResponse = new AuthResponseDTO();
        authResponse.setUserId(123L);
        authResponse.setFullName("John Doe");

        when(authService.login("john", "pass"))
                .thenReturn(authResponse);

        mockMvc.perform(post("/user/login")
                        .with(csrf())
                        .param("username", "john")
                        .param("password", "pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/cabinet"))
                .andExpect(request().sessionAttribute("USER_ID", 123L))
                .andExpect(request().sessionAttribute("USER_NAME", "John Doe"));
    }

    @Test
    void shouldReturnLoginPageWithErrorWhenAuthFails() throws Exception {
        when(authService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("Auth failed"));

        mockMvc.perform(post("/user/login")
                        .with(csrf())
                        .param("username", "wrong")
                        .param("password", "wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void shouldReturnCabinetPageWhenUserInSession() throws Exception {
        Booking booking = new Booking();
        booking.setBookRef("REF123");

        Ticket ticket = new Ticket();
        ticket.setTicketNo("T123");
        ticket.setBooking(booking);
        ticket.setPassengerId("123");
        ticket.setPassengerName("John Doe");

        List<Ticket> tickets = List.of(ticket);

        when(ticketService.findAllByUserId("123"))
                .thenReturn(tickets);

        mockMvc.perform(get("/user/cabinet")
                        .sessionAttr("USER_ID", 123L)
                        .sessionAttr("USER_NAME", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/cabinet"))
                .andExpect(model().attribute("fullName", "John Doe"))
                .andExpect(model().attributeExists("tickets"));
    }

    @Test
    void shouldRedirectToLoginWhenNoSession() throws Exception {
        mockMvc.perform(get("/user/cabinet"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }

    @Test
    void shouldLogoutAndRedirect() throws Exception {
        mockMvc.perform(get("/user/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }
}