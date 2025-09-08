package com.myapp.Airports.controller.web;

import com.myapp.Airports.model.Flying;
import com.myapp.Airports.service.FlyingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FlyingControllerTest {

    @Mock
    private FlyingService flyingService;

    @InjectMocks
    private FlyingController flyingController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(flyingController).build();
    }

    @Test
    void testListFlights() throws Exception {
        when(flyingService.findAll()).thenReturn(Arrays.asList(new Flying()));

        mockMvc.perform(get("/flights"))
                .andExpect(status().isOk())
                .andExpect(view().name("flight_list"))
                .andExpect(model().attributeExists("flights"));

        verify(flyingService, times(1)).findAll();
    }

    @Test
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/flights/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("flight_form"))
                .andExpect(model().attributeExists("flyingDTO"));
    }

    @Test
    void testCreateFlightValid() throws Exception {
        mockMvc.perform(post("/flights/new")
                        .param("flightId", "1")
                        .param("flightNo", "AA123")
                        .param("scheduledDeparture", "2025-09-02T10:00:00")
                        .param("scheduledArrival", "2025-09-02T14:00:00")
                        .param("departureAirport", "JFK")
                        .param("arrivalAirport", "LAX")
                        .param("status", "SCHEDULED")
                        .param("aircraftCode", "A320"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/flights"));

        verify(flyingService, times(1)).save(ArgumentMatchers.any(Flying.class));
    }

    @Test
    void testShowEditFormFound() throws Exception {
        Flying flight = new Flying();
        flight.setFlightId(1);
        flight.setDepartureAirport("JFK");
        flight.setArrivalAirport("LAX");
        flight.setFlightNumber("AA123");
        flight.setScheduledDeparture(LocalDateTime.of(2025, 9, 2, 10, 0));
        flight.setScheduledArrival(LocalDateTime.of(2025, 9, 2, 14, 0));
        when(flyingService.findById(1)).thenReturn(Optional.of(flight));

        mockMvc.perform(get("/flights/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("flight_form"))
                .andExpect(model().attributeExists("flyingDTO"));
    }

 /*   @Test
    void testShowEditFormNotFound() throws Exception {
        when(flyingService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/flights/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/flight_list"));
    }*/

    @Test
    void testUpdateFlightValid() throws Exception {
        mockMvc.perform(post("/flights/new")
                        .param("flightId", "1")
                        .param("flightNo", "AA123")
                        .param("scheduledDeparture", "2025-09-02T10:00:00")
                        .param("scheduledArrival", "2025-09-02T14:00:00")
                        .param("departureAirport", "JFK")
                        .param("arrivalAirport", "LAX")
                        .param("status", "SCHEDULED")
                        .param("aircraftCode", "A320"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/flights"));

        verify(flyingService, times(1)).save(ArgumentMatchers.any(Flying.class));
    }

 /*   @Test
    void testDeleteFlight() throws Exception {
        mockMvc.perform(post("/flights/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/flight_list"));

        verify(flyingService, times(1)).deleteById(1);
    }*/
}
