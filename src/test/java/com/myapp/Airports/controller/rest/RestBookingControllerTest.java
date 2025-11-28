package com.myapp.Airports.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.Airports.controller.rest.RestBookingController;
import com.myapp.Airports.dto.BookingDTO;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.mapper.BookingMapper;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestBookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private SeatService seatService;

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    private Booking bookingEntity() {
        Booking b = new Booking();
        b.setBookRef("ABC123");
        b.setBookDate(LocalDateTime.of(2025, 3, 1, 12, 0));
        b.setTotalAmount(new BigDecimal("150.00"));
        return b;
    }

    private BookingDTO bookingDto() {
        return new BookingDTO(
                "ABC123",
                LocalDateTime.of(2025, 3, 1, 12, 0),
                new BigDecimal("150.00")
        );
    }

    @Test
    void testGetAllBookings() throws Exception {
        Booking b = bookingEntity();
        when(bookingService.findAll()).thenReturn(List.of(b));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookRef").value("ABC123"))
                .andExpect(jsonPath("$[0].totalAmount").value(150.00));
    }

    @Test
    void testGetBooking() throws Exception {
        Booking b = bookingEntity();
        when(bookingService.findByBookRef("ABC123")).thenReturn(b);

        mockMvc.perform(get("/api/bookings/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookRef").value("ABC123"))
                .andExpect(jsonPath("$.totalAmount").value(150.00));
    }

    @Test
    void testGetBooking_NotFound() throws Exception {
        when(bookingService.findByBookRef("ZZZ999")).thenReturn(null);

        mockMvc.perform(get("/api/bookings/ZZZ999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBooking() throws Exception {
        Booking b = bookingEntity();
        when(bookingService.save(any(Booking.class))).thenReturn(b);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookRef").value("ABC123"));
    }

    @Test
    void testCreateBooking_ValidationError() throws Exception {
        BookingDTO invalidDto = new BookingDTO("", null, new BigDecimal("-10"));

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBooking() throws Exception {
        Booking updated = bookingEntity();
        updated.setTotalAmount(new BigDecimal("200.00"));

        when(bookingService.updateBookingAndReturn(eq("ABC123"), any(Booking.class)))
                .thenReturn(updated);

        BookingDTO dto = bookingDto();
        dto.setTotalAmount(new BigDecimal("200.00"));

        mockMvc.perform(put("/api/bookings/ABC123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(200.00));
    }

    @Test
    void testDeleteBooking() throws Exception {
        doNothing().when(bookingService).delete("ABC123");

        mockMvc.perform(delete("/api/bookings/ABC123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCancelBooking() throws Exception {
        doNothing().when(bookingService).cancelBooking("ABC123");

        mockMvc.perform(post("/api/bookings/ABC123/cancel"))
                .andExpect(status().isNoContent());
    }
}
