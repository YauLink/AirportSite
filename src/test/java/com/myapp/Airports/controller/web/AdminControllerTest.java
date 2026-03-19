package com.myapp.Airports.controller.web;

import com.myapp.Airports.controller.web.AdminController;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.FlyingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@WithMockUser
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlyingService flyingService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private AuthService authService;

    @Test
    void shouldReturnAdminIndexView() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"));
    }

    @Test
    void shouldReturnDashboardView() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"));
    }

    @Test
    void shouldReturnFlightsView() throws Exception {
        mockMvc.perform(get("/admin/flights"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/flights"))
                .andExpect(model().attributeExists("flights"));
    }

    @Test
    void shouldReturnBookingsReportView() throws Exception {
        Page<Booking> mockPage = new PageImpl<>(List.of());

        when(bookingService.findAll(0, 20)).thenReturn(mockPage);

        mockMvc.perform(get("/admin/reports/bookings")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reports/bookings"))
                .andExpect(model().attributeExists("bookings"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void shouldReturnSettingsView() throws Exception {
        mockMvc.perform(get("/admin/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/settings"))
                .andExpect(model().attributeExists("theme"))
                .andExpect(model().attributeExists("notificationsEnabled"));
    }
}