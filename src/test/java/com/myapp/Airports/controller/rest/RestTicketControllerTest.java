package com.myapp.Airports.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.Airports.dto.TicketDTO;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestTicketController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestTicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService service;

    private ObjectMapper mapper = new ObjectMapper();

    private Ticket ticket;
    private TicketDTO dto;
    private Booking booking;

    @BeforeEach
    void setup() {
        booking = new Booking();
        booking.setBookRef("BR123");

        ticket = new Ticket();
        ticket.setTicketNo("TCK123");
        ticket.setBooking(booking);
        ticket.setPassengerId("PID999");
        ticket.setPassengerName("John Doe");
        ticket.setContactData("{\"note\":{\"phone\":\"12345\"}}");

        dto = new TicketDTO(
                "TCK123",
                "BR123",
                "PID999",
                "John Doe",
                "12345"
        );
    }

    @Test
    void testGetAllTickets() throws Exception {
        when(service.findAll()).thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/tickets/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketNo").value("TCK123"))
                .andExpect(jsonPath("$[0].passengerName").value("John Doe"));
    }

    @Test
    void testGetTicket() throws Exception {
        when(service.findById("TCK123")).thenReturn(ticket);

        mockMvc.perform(get("/api/tickets/api/TCK123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketNo").value("TCK123"))
                .andExpect(jsonPath("$.bookRef").value("BR123"));
    }

    @Test
    void testGetTicket_NotFound() throws Exception {
        when(service.findById("BAD")).thenThrow(new RuntimeException("Ticket not found"));

        mockMvc.perform(get("/api/tickets/api/BAD"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTicket() throws Exception {
        // We expect that controller rewrites contactData -> {"note":{"phone":"value"}}
        Ticket saved = new Ticket();
        saved.setTicketNo("TCK123");
        saved.setBooking(booking);
        saved.setPassengerId("PID999");
        saved.setPassengerName("John Doe");
        saved.setContactData("{\"note\":{\"phone\":\"12345\"}}");

        when(service.save(any(Ticket.class))).thenReturn(saved);

        mockMvc.perform(
                        post("/api/tickets/api")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketNo").value("TCK123"))
                .andExpect(jsonPath("$.contactData").value("{\"note\":{\"phone\":\"12345\"}}"));
    }

    @Test
    void testUpdateTicket() throws Exception {
        when(service.findById("TCK123")).thenReturn(ticket);

        Ticket updated = new Ticket();
        updated.setTicketNo("TCK123");
        updated.setBooking(booking);
        updated.setPassengerId("PID999");
        updated.setPassengerName("John Doe UPDATED");
        updated.setContactData("{\"note\":{\"phone\":\"55555\"}}");

        TicketDTO updateDto = new TicketDTO(
                "TCK123",
                "BR123",
                "PID999",
                "John Doe UPDATED",
                "55555"
        );

        when(service.save(any(Ticket.class))).thenReturn(updated);

        mockMvc.perform(
                        put("/api/tickets/api/TCK123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passengerName").value("John Doe UPDATED"))
                .andExpect(jsonPath("$.contactData").value("{\"note\":{\"phone\":\"55555\"}}"));
    }

    @Test
    void testUpdateTicket_NotFound() throws Exception {
        when(service.findById("NOPE")).thenThrow(new RuntimeException("Ticket not found"));

        mockMvc.perform(
                        put("/api/tickets/api/NOPE")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTicket() throws Exception {
        doNothing().when(service).delete("TCK123");

        mockMvc.perform(delete("/api/tickets/api/TCK123"))
                .andExpect(status().isNoContent());
    }
}