package com.myapp.Airports.controller.web;

import com.myapp.Airports.view.api.IAirportsView;
import com.myapp.Airports.view.api.IFlyingsView;
import com.myapp.Airports.view.api.IFlyingsView.FlyingFilter;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilterController.class)
@WithMockUser
class FilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAirportsView airportView;

    @MockBean
    private IFlyingsView flyingsView;

    @Test
    void shouldShowFilterFormWithoutParams() throws Exception {
        when(airportView.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/filters"))
                .andExpect(status().isOk())
                .andExpect(view().name("filters"))
                .andExpect(model().attributeExists("airports"))
                .andExpect(model().attributeDoesNotExist("flying", "currentAirportOut", "currentAirportIn"));
    }

    @Test
    void shouldShowFilteredResults() throws Exception {
        when(airportView.getAll()).thenReturn(Collections.emptyList());
        when(flyingsView.getList(ArgumentMatchers.any(FlyingFilter.class))).thenReturn(Collections.emptyList());
        when(flyingsView.count(ArgumentMatchers.any(FlyingFilter.class))).thenReturn(0L);

        mockMvc.perform(get("/filters")
                        .param("airport_out", "JFK")
                        .param("airport_in", "LAX")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("filters"))
                .andExpect(model().attributeExists(
                        "airports", "flying", "maxCountFlying", "currentPage", "currentAirportOut", "currentAirportIn"
                ));
    }

    @Test
    void shouldHandlePostFilter() throws Exception {
        when(airportView.getAll()).thenReturn(Collections.emptyList());
        when(flyingsView.getList(ArgumentMatchers.any(FlyingFilter.class))).thenReturn(Collections.emptyList());
        when(flyingsView.count(ArgumentMatchers.any(FlyingFilter.class))).thenReturn(0L);

        mockMvc.perform(post("/filters")
                        .param("airport_out", "JFK")
                        .param("airport_in", "LAX")
                        .param("page", "1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("filters"))
                .andExpect(model().attributeExists(
                        "airports", "flying", "maxCountFlying", "currentPage", "currentAirportOut", "currentAirportIn"
                ));
    }
}
