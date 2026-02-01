package com.myapp.Airports.service;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.TicketFlight;
import com.myapp.Airports.storage.api.IBookingRepository;
import com.myapp.Airports.storage.api.ITicketFlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private IBookingRepository bookingRepository;

    @Mock
    private ITicketFlightRepository ticketFlightRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Booking b1 = new Booking();
        Booking b2 = new Booking();

        int page = 0;
        int size = 2;

        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by("bookDate").descending());

        Page<Booking> bookingPage =
                new PageImpl<>(Arrays.asList(b1, b2), pageRequest, 2);

        when(bookingRepository.findAll(pageRequest)).thenReturn(bookingPage);

        Page<Booking> result = bookingService.findAll(page, size);

        assertEquals(2, result.getContent().size());
        verify(bookingRepository).findAll(pageRequest);
    }

    @Test
    void testFindByBookRef_found() {
        Booking booking = new Booking();
        when(bookingRepository.findById("REF123")).thenReturn(Optional.of(booking));

        Booking result = bookingService.findByBookRef("REF123");

        assertNotNull(result);
        verify(bookingRepository).findById("REF123");
    }

    @Test
    void testFindByBookRef_notFound() {
        when(bookingRepository.findById("NOTFOUND")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.findByBookRef("NOTFOUND"));
    }

    @Test
    void testUpdateBooking() {
        Booking existing = new Booking();
        existing.setBookDate(LocalDateTime.now());
        existing.setTotalAmount(new BigDecimal(100));

        Booking updated = new Booking();
        updated.setBookDate(LocalDateTime.now().plusDays(1));
        updated.setTotalAmount(new BigDecimal(200));

        when(bookingRepository.findById("REF123")).thenReturn(Optional.of(existing));

        bookingService.updateBooking("REF123", updated);

        verify(bookingRepository).save(existing);
        assertEquals(new BigDecimal(200), existing.getTotalAmount());
    }

    @Test
    void testCancelBooking() {
        bookingService.cancelBooking("REF123");
        verify(bookingRepository).deleteById("REF123");
    }

    @Test
    void testSave() {
        Booking booking = new Booking();
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking saved = bookingService.save(booking);

        assertEquals(booking, saved);
        verify(bookingRepository).save(booking);
    }

    @Test
    void testDelete() {
        bookingService.delete("REF123");
        verify(bookingRepository).deleteById("REF123");
    }

    @Test
    void testAssignSeat() {
        TicketFlight tf1 = new TicketFlight();
        TicketFlight tf2 = new TicketFlight();
        List<TicketFlight> flights = Arrays.asList(tf1, tf2);

        when(ticketFlightRepository.findByBookingRef("REF123")).thenReturn(flights);

        bookingService.assignSeat("REF123", "12A");

        assertEquals("12A", tf1.getSeatNo());
        assertEquals("12A", tf2.getSeatNo());
        verify(ticketFlightRepository).saveAll(flights);
    }

    @Test
    void testFindByFlightId() {
        Booking booking = new Booking();
        when(ticketFlightRepository.findBookingsByFlight(123))
                .thenReturn(Arrays.asList(booking));

        List<Booking> result = bookingService.findByFlightId(123);

        assertEquals(1, result.size());
        verify(ticketFlightRepository).findBookingsByFlight(123);
    }
}
