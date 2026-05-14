package com.myapp.Airports.controller.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnDashboardData() throws Exception {
        mockMvc.perform(get("/api/admin/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").value(3))
                .andExpect(jsonPath("$.totalFlights").value(3))
                .andExpect(jsonPath("$.systemStatus").value("OK"));
    }

    @Test
    void shouldReturnUsers() throws Exception {
        mockMvc.perform(get("/api/admin/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Alice"))
                .andExpect(jsonPath("$[1]").value("Bob"))
                .andExpect(jsonPath("$[2]").value("Charlie"));
    }

    @Test
    void shouldReturnFlights() throws Exception {
        mockMvc.perform(get("/api/admin/api/flights"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0]").value("Flight-101"))
                .andExpect(jsonPath("$[1]").value("Flight-202"))
                .andExpect(jsonPath("$[2]").value("Flight-303"));
    }

    @Test
    void shouldReturnSettings() throws Exception {
        mockMvc.perform(get("/api/admin/api/settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.theme").value("dark"))
                .andExpect(jsonPath("$.notificationsEnabled").value(true));
    }
}