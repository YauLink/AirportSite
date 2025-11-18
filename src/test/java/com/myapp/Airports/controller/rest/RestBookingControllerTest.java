package com.myapp.Airports.controller.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.Airports.controller.rest.RestBookingController;
import com.myapp.Airports.dto.BookingDTO;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.mapper.BookingMapper;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.SeatService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestBookingController.class)
public class RestBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private SeatService seatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllBookings() throws Exception {
        Booking b = new Booking();
        b.setBookRef("ABC");

        Mockito.when(bookingService.findAll()).thenReturn(List.of(b));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookRef").value("ABC"));
    }

    @Test
    void testGetBooking_Found() throws Exception {
        Booking b = new Booking();
        b.setBookRef("ABC");

        Mockito.when(bookingService.findByBookRef("ABC")).thenReturn(b);

        mockMvc.perform(get("/api/bookings/ABC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookRef").value("ABC"));
    }

    @Test
    void testGetBooking_NotFound() throws Exception {
        Mockito.when(bookingService.findByBookRef("XYZ")).thenReturn(null);

        mockMvc.perform(get("/api/bookings/XYZ"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBooking() throws Exception {
        BookingDTO dto = new BookingDTO();
        dto.setBookRef("NEW");

        Booking entity = BookingMapper.toEntity(dto);

        Mockito.when(bookingService.save(any(Booking.class))).thenReturn(entity);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookRef").value("NEW"));
    }

    @Test
    void testUpdateBooking() throws Exception {
        BookingDTO dto = new BookingDTO();
        dto.setBookRef("UPD");

        Booking updated = BookingMapper.toEntity(dto);

        Mockito.when(bookingService.updateBookingAndReturn(eq("UPD"), any(Booking.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/bookings/UPD")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookRef").value("UPD"));
    }

    @Test
    void testDeleteBooking() throws Exception {

        mockMvc.perform(delete("/api/bookings/DEL"))
                .andExpect(status().isNoContent());

        Mockito.verify(bookingService).delete("DEL");
    }

    @Test
    void testCancelBooking() throws Exception {

        mockMvc.perform(post("/api/bookings/ABC/cancel"))
                .andExpect(status().isNoContent());

        Mockito.verify(bookingService).cancelBooking("ABC");
    }
}
