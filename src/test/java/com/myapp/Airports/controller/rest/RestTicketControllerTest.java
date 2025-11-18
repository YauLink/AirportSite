package com.myapp.Airports.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestTicketController.class)
class RestTicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    void testGetAllTickets() throws Exception {
        Ticket t = new Ticket();
        t.setTicketNo("TK100");
        t.setPassengerName("John Doe");

        when(ticketService.findAll()).thenReturn(List.of(t));

        mockMvc.perform(get("/api/tickets/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketNo").value("TK100"))
                .andExpect(jsonPath("$[0].passengerName").value("John Doe"));
    }

    @Test
    void testGetTicketById() throws Exception {
        Ticket t = new Ticket();
        t.setTicketNo("T200");
        t.setPassengerName("Alice");

        when(ticketService.findById("T200")).thenReturn(t);

        mockMvc.perform(get("/api/tickets/api/T200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketNo").value("T200"))
                .andExpect(jsonPath("$.passengerName").value("Alice"));
    }

    @Test
    void testGetTicketNotFound() throws Exception {
        when(ticketService.findById("NOTEXIST")).thenReturn(null);

        mockMvc.perform(get("/api/tickets/api/NOTEXIST"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTicket() throws Exception {
        Ticket saved = new Ticket();
        saved.setTicketNo("T300");
        saved.setPassengerName("Bob");
        saved.setContactData("{\"note\": {\"phone\": \"123\"}}");

        when(ticketService.save(any())).thenReturn(saved);

        mockMvc.perform(post("/api/tickets/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "ticketNo": "T300",
                          "passengerName": "Bob",
                          "contactData": "123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketNo").value("T300"))
                .andExpect(jsonPath("$.passengerName").value("Bob"));
    }

    @Test
    void testUpdateTicket() throws Exception {
        Ticket existing = new Ticket();
        existing.setTicketNo("UPD1");
        existing.setPassengerName("Old Name");

        when(ticketService.findById("UPD1")).thenReturn(existing);
        when(ticketService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/tickets/api/UPD1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "ticketNo": "UPD1",
                          "passengerName": "New Name",
                          "contactData": "555"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passengerName").value("New Name"));
    }

    @Test
    void testUpdateTicketNotFound() throws Exception {
        when(ticketService.findById("MISSING")).thenReturn(null);

        mockMvc.perform(put("/api/tickets/api/MISSING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "ticketNo": "MISSING",
                          "passengerName": "Someone"
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTicket() throws Exception {
        mockMvc.perform(delete("/api/tickets/api/DEL-100"))
                .andExpect(status().isNoContent());

        verify(ticketService).delete("DEL-100");
    }
}