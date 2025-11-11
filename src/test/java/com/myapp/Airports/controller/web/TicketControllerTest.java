package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.TicketDTO;
import com.myapp.Airports.mapper.TicketMapper;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(TicketController.class)
@WithMockUser   // ensures security allows POST/DELETE
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService service;

    @Test
    void shouldShowTicketList() throws Exception {
        Page<Ticket> emptyPage = new PageImpl<>(Collections.emptyList());

        when(service.getAllTickets(anyInt(), anyInt())).thenReturn(emptyPage);


        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets/list"))
                .andExpect(model().attributeExists("tickets"));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        Booking booking = new Booking();
        booking.setBookRef("B001");

        Ticket ticket = new Ticket();
        ticket.setTicketNo("T123");
        ticket.setBooking(booking);
        ticket.setPassengerId("P001");
        ticket.setPassengerName("John Doe");
        ticket.setContactData("{\"email\":\"john@example.com\"}");

        when(service.findById("T123")).thenReturn(ticket);

        mockMvc.perform(get("/tickets/T123/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets/edit"))
                .andExpect(model().attributeExists("ticket"))
                .andExpect(model().attribute("ticket", hasProperty("ticketNo", is("T123"))))
                .andExpect(model().attribute("ticket", hasProperty("ticketNo", is("T123"))))
                .andExpect(model().attribute("ticket", hasProperty("passengerId", is("P001"))))
                .andExpect(model().attribute("ticket", hasProperty("passengerName", is("John Doe"))))
                .andExpect(model().attribute("ticket", hasProperty("contactData", is("email: john@example.com"))));
    }

    @Test
    void shouldUpdateTicket() throws Exception {
        Booking booking = new Booking();
        booking.setBookRef("B001");

        Ticket ticket = new Ticket();
        ticket.setTicketNo("T123");
        ticket.setBooking(booking);

        when(service.findById("T123")).thenReturn(ticket);
        when(service.getBooking("B001")).thenReturn(booking);

        mockMvc.perform(post("/tickets/T123/update")
                        .param("ticketNo", "T123")
                        .param("bookRef", "B001")
                        .param("passengerName", "John Doe")
                        .param("contactData", "123456789")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tickets/list"));

        Mockito.verify(service).save(Mockito.any(Ticket.class));
    }

    @Test
    void shouldDeleteTicket() throws Exception {
        mockMvc.perform(post("/tickets/T123/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tickets/list"));

        Mockito.verify(service).delete("T123");
    }
}
