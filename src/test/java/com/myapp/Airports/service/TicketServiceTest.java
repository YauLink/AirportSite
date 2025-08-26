package com.myapp.Airports.service;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.storage.api.IBookingRepository;
import com.myapp.Airports.storage.api.ITicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private ITicketRepository ticketRepo;

    @Mock
    private IBookingRepository bookingRepo;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Ticket t1 = new Ticket();
        Ticket t2 = new Ticket();
        when(ticketRepo.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Ticket> tickets = ticketService.findAll();

        assertEquals(2, tickets.size());
        verify(ticketRepo, times(1)).findAll();
    }

    @Test
    void testFindById_found() {
        Ticket ticket = new Ticket();
        when(ticketRepo.findById("T123")).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.findById("T123");

        assertNotNull(result);
        verify(ticketRepo).findById("T123");
    }

    @Test
    void testFindById_notFound() {
        when(ticketRepo.findById("T404")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.findById("T404"));
    }

    @Test
    void testSave() {
        Ticket ticket = new Ticket();
        when(ticketRepo.save(ticket)).thenReturn(ticket);

        Ticket saved = ticketService.save(ticket);

        assertEquals(ticket, saved);
        verify(ticketRepo).save(ticket);
    }

    @Test
    void testDelete() {
        ticketService.delete("T123");
        verify(ticketRepo).deleteById("T123");
    }

    @Test
    void testGetBooking_found() {
        Booking booking = new Booking();
        when(bookingRepo.findById("B001")).thenReturn(Optional.of(booking));

        Booking result = ticketService.getBooking("B001");

        assertNotNull(result);
        verify(bookingRepo).findById("B001");
    }

    @Test
    void testGetBooking_notFound() {
        when(bookingRepo.findById("B404")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.getBooking("B404"));
    }
}
