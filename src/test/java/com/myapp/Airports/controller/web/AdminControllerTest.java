package com.myapp.Airports.controller.web;

import com.myapp.Airports.controller.web.AdminController;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.FlyingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminController.class)
@WithMockUser
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlyingService flyingService;

    @MockBean
    private BookingService bookingService;

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
}