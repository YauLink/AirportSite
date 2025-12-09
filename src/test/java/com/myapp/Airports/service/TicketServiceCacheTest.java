package com.myapp.Airports.service;

import com.myapp.Airports.config.RedisTestConfig;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.storage.api.IBookingRepository;
import com.myapp.Airports.storage.api.ITicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(RedisTestConfig.class)
class TicketServiceCacheTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private ITicketRepository ticketRepo;

    @MockBean
    private IBookingRepository bookingRepo;

    private Ticket ticket1;
    private Ticket ticket2;

    @BeforeEach
    void setup() {
        cacheManager.getCache("tickets").clear();
        cacheManager.getCache("ticket").clear();

        ticket1 = new Ticket();
        ticket1.setTicketNo("TICK1");

        ticket2 = new Ticket();
        ticket2.setTicketNo("TICK2");
    }

    @Test
    void findAll_ShouldUseCache() {
        List<Ticket> tickets = List.of(ticket1, ticket2);
        when(ticketRepo.findAll()).thenReturn(tickets);

        List<Ticket> firstCall = ticketService.findAll();
        assertEquals(2, firstCall.size());
        verify(ticketRepo, times(1)).findAll();

        List<Ticket> secondCall = ticketService.findAll();
        assertEquals(2, secondCall.size());
        verify(ticketRepo, times(1)).findAll();
    }

    @Test
    void findById_ShouldUseCache() {
        when(ticketRepo.findById("TICK1")).thenReturn(Optional.of(ticket1));

        Ticket firstCall = ticketService.findById("TICK1");
        assertEquals("TICK1", firstCall.getTicketNo());
        verify(ticketRepo, times(1)).findById("TICK1");

        Ticket secondCall = ticketService.findById("TICK1");
        assertEquals("TICK1", secondCall.getTicketNo());
        verify(ticketRepo, times(1)).findById("TICK1");
    }

    @Test
    void save_ShouldEvictCache() {
        when(ticketRepo.save(ticket1)).thenReturn(ticket1);
        when(ticketRepo.findAll()).thenReturn(List.of(ticket1));

        ticketService.findAll();
        assertNotNull(cacheManager.getCache("tickets").get(SimpleKey.EMPTY));

        ticketService.save(ticket1);
        assertNull(cacheManager.getCache("tickets").get(SimpleKey.EMPTY));
    }

    @Test
    void delete_ShouldEvictCache() {
        doNothing().when(ticketRepo).deleteById("TICK1");

        ticketService.findAll();
        ticketService.delete("TICK1");
        assertNull(cacheManager.getCache("tickets").get(""));
    }

    @Test
    void getBooking_ShouldUseCache() {
        Booking booking = new Booking();
        booking.setBookRef("REF1");
        when(bookingRepo.findById("REF1")).thenReturn(Optional.of(booking));

        Booking first = ticketService.getBooking("REF1");
        Booking second = ticketService.getBooking("REF1");

        assertEquals("REF1", first.getBookRef());
        assertEquals("REF1", second.getBookRef());
        verify(bookingRepo, times(1)).findById("REF1");
    }
}
