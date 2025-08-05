package com.myapp.Airports.controller.web;

import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(TicketControllerTest.class)
@WithMockUser
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService service;

    @Test
    void shouldShowTicketList() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tickets/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets/list"))
                .andExpect(model().attributeExists("tickets"));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setTicketNo("T123");
        when(service.findById("T123")).thenReturn(ticket);

        mockMvc.perform(get("/tickets/T123/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets/edit"))
                .andExpect(model().attributeExists("ticket"));
    }

    @Test
    void shouldUpdateTicket() throws Exception {
        // just test redirection; service.save can be mocked if needed
        mockMvc.perform(post("/tickets/T123/edit")
                        .param("ticketNo", "T123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tickets/list"));
    }

    @Test
    void shouldDeleteTicket() throws Exception {
        mockMvc.perform(post("/tickets/T123/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tickets/list"));

        Mockito.verify(service).delete("T123");
    }
}
