package com.myapp.Airports.controller.web;

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
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
}
