package com.myapp.Airports.service;

import com.myapp.Airports.config.RedisTestConfig;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.storage.api.IBookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(RedisTestConfig.class)
class BookingServiceCacheTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private IBookingRepository bookingRepository;

    @BeforeEach
    void clearCaches() {
        if (cacheManager.getCache("bookings") != null)
            cacheManager.getCache("bookings").clear();
        if (cacheManager.getCache("booking") != null)
            cacheManager.getCache("booking").clear();
    }

    @Test
    void findAll_ShouldUseCache() {
        Booking b1 = new Booking("REF1", LocalDateTime.now(), BigDecimal.valueOf(100.0));
        Booking b2 = new Booking("REF2", LocalDateTime.now(), BigDecimal.valueOf(200.0));
        Page<Booking> page = new PageImpl<>(List.of(b1, b2));

        when(bookingRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Booking> firstCall = bookingService.findAll(0, 10);
        Page<Booking> secondCall = bookingService.findAll(0, 10);

        verify(bookingRepository, times(1)).findAll(any(Pageable.class));
        assertEquals(firstCall.getContent(), secondCall.getContent());
    }

    @Test
    void findByBookRef_ShouldUseCache() {
        Booking booking = new Booking("REF123", LocalDateTime.now(), BigDecimal.valueOf(150.0));
        when(bookingRepository.findById("REF123")).thenReturn(Optional.of(booking));

        Booking firstCall = bookingService.findByBookRef("REF123");
        Booking secondCall = bookingService.findByBookRef("REF123");

        verify(bookingRepository, times(1)).findById("REF123");
        assertEquals(firstCall, secondCall);
    }

    @Test
    void updateBooking_ShouldEvictCache() {
        Booking booking = new Booking("REFX", LocalDateTime.now(), BigDecimal.valueOf(100.0));
        when(bookingRepository.findById("REFX")).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        bookingService.findByBookRef("REFX");
        bookingService.updateBooking("REFX", new Booking("REFX", LocalDateTime.now(), BigDecimal.valueOf(200.0)));

        bookingService.findByBookRef("REFX");
        verify(bookingRepository, times(3)).findById("REFX");
    }
}
