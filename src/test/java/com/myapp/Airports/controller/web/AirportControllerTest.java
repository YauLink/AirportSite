package com.myapp.Airports.controller.web;

import com.myapp.Airports.controller.web.AirportController;
import com.myapp.Airports.view.api.IAirportsView;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AirportController.class)
@WithMockUser
class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAirportsView airportView;

    @Test
    void shouldReturnAllAirportsView() throws Exception {
        when(airportView.getAll()).thenReturn(List.of()); // mock as empty list for now

        mockMvc.perform(get("/airports"))
                .andExpect(status().isOk())
                .andExpect(view().name("airports"))
                .andExpect(model().attributeExists("airports"));
    }

    /*@Test
    void shouldReturnSingleAirportView() throws Exception {
        when(airportView.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/airports/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("airport"))
                .andExpect(model().attributeExists("airports"));
    }*/
}
