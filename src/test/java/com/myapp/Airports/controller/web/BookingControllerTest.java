package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.BookingDTO;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.SeatService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private SeatService seatService;

    @Test
    public void testEditBooking() throws Exception {
        Booking booking = new Booking();
        booking.setBookRef("REF123");

        Mockito.when(bookingService.findByBookRef("REF123")).thenReturn(booking);

        mockMvc.perform(get("/bookings/edit/REF123"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/edit"));
    }

    @Test
    public void testShowSeatSelection() throws Exception {
        Mockito.when(seatService.findAvailableForFlight(1)).thenReturn(List.of());

        mockMvc.perform(get("/bookings/seats/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/seats"));
    }

    @Test
    public void testAssignSeat() throws Exception {
        mockMvc.perform(post("/bookings/assign/REF123")
                        .param("seatNo", "12A"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/list"));

        Mockito.verify(bookingService).assignSeat("REF123", "12A");
    }

    @Test
    void shouldReturnBookingsListView() throws Exception {
        Page<Booking> mockPage = new PageImpl<>(List.of());

        Mockito.when(bookingService.findAll(0, 30)).thenReturn(mockPage);

        mockMvc.perform(get("/bookings/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/list"))
                .andExpect(model().attributeExists("bookingsPage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void shouldDeleteBooking() throws Exception {
        mockMvc.perform(delete("/bookings/REF123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/list"));

        Mockito.verify(bookingService).delete("REF123");
    }

    @Test
    void shouldUpdateBooking() throws Exception {
        mockMvc.perform(post("/bookings/update/REF123")
                        .param("bookRef", "REF123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/list"));

        Mockito.verify(bookingService).updateBooking(Mockito.eq("REF123"), Mockito.any(Booking.class));
    }

    @Test
    void shouldCreateBooking() throws Exception {
        mockMvc.perform(post("/bookings")
                        .param("bookRef", "REF123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/list"));

        Mockito.verify(bookingService).save(Mockito.any(Booking.class));
    }

    @Test
    void shouldCancelBooking() throws Exception {
        mockMvc.perform(post("/bookings/cancel/REF123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/list"));

        Mockito.verify(bookingService).cancelBooking("REF123");
    }
}
