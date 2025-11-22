package com.myapp.Airports.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.Airports.dto.FlyingDTO;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.service.FlyingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestFlyingController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestFlyingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlyingService flyingService;

    private ObjectMapper mapper;

    private Flying flying;
    private FlyingDTO dto;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // <-- handles LocalDateTime

        flying = new Flying();
        flying.setId(1);
        flying.setFlightNo("AA123");
        flying.setDepartureAirport("JFK");
        flying.setArrivalAirport("LAX");
        flying.setScheduledDeparture(LocalDateTime.of(2025, 9, 2, 10, 0));
        flying.setScheduledArrival(LocalDateTime.of(2025, 9, 2, 14, 0));
        flying.setStatus("SCHEDULED");
        flying.setAircraftCode("A320");

        dto = new FlyingDTO();
        dto.setFlightId(1);
        dto.setFlightNo("AA123");
        dto.setDepartureAirport("JFK");
        dto.setArrivalAirport("LAX");
        dto.setScheduledDeparture(LocalDateTime.of(2025, 9, 2, 10, 0));
        dto.setScheduledArrival(LocalDateTime.of(2025, 9, 2, 14, 0));
        dto.setStatus("SCHEDULED");
        dto.setAircraftCode("A320");
    }

    @Test
    void testGetAllFlights() throws Exception {
        when(flyingService.findAll()).thenReturn(List.of(flying));

        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flightNo").value("AA123"))
                .andExpect(jsonPath("$[0].departureAirport").value("JFK"));
    }

    @Test
    void testGetFlightById_Found() throws Exception {
        when(flyingService.findById(1)).thenReturn(Optional.of(flying));

        mockMvc.perform(get("/api/flights/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNo").value("AA123"))
                .andExpect(jsonPath("$.arrivalAirport").value("LAX"));
    }

    @Test
    void testGetFlightById_NotFound() throws Exception {
        when(flyingService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/flights/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateFlight() throws Exception {
        when(flyingService.saveAndReturn(any(Flying.class))).thenReturn(flying);

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNo").value("AA123"))
                .andExpect(jsonPath("$.departureAirport").value("JFK"));
    }

    @Test
    void testUpdateFlight_Exists() throws Exception {
        when(flyingService.findById(1)).thenReturn(Optional.of(flying));
        when(flyingService.saveAndReturn(any(Flying.class))).thenReturn(flying);

        dto.setStatus("DELAYED"); // update status

        mockMvc.perform(put("/api/flights/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SCHEDULED")); // mapper returns flying object
    }

    @Test
    void testUpdateFlight_NotExists() throws Exception {
        when(flyingService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/flights/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFlight() throws Exception {
        doNothing().when(flyingService).deleteById(1);

        mockMvc.perform(delete("/api/flights/1"))
                .andExpect(status().isNoContent());
    }
}
