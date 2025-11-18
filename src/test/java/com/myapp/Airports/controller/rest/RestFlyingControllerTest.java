package com.myapp.Airports.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.Airports.controller.rest.RestFlyingController;
import com.myapp.Airports.dto.FlyingDTO;
import com.myapp.Airports.mapper.FlyingMapper;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.view.api.IAirportsView;
import com.myapp.Airports.view.api.IFlyingsView;
import com.myapp.Airports.service.FlyingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestFlyingController.class)
public class RestFlyingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlyingService flyingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllFlights() throws Exception {

        Flying f = new Flying();
        f.setId(1);

        Mockito.when(flyingService.findAll()).thenReturn(List.of(f));

        mockMvc.perform(get("/api/flights/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        Mockito.verify(flyingService).findAll();
    }

    @Test
    void testGetFlightById_Found() throws Exception {

        Flying f = new Flying();
        f.setId(5);

        Mockito.when(flyingService.findById(5)).thenReturn(Optional.of(f));

        mockMvc.perform(get("/api/flights/api/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));

        Mockito.verify(flyingService).findById(5);
    }

    @Test
    void testGetFlightById_NotFound() throws Exception {

        Mockito.when(flyingService.findById(20)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/flights/api/20"))
                .andExpect(status().isNotFound());

        Mockito.verify(flyingService).findById(20);
    }

    @Test
    void testCreateFlight() throws Exception {

        FlyingDTO dto = new FlyingDTO();
        dto.setId(10);

        Flying saved = FlyingMapper.toEntity(dto);
        saved.setId(10); // ensure id returned

        Mockito.when(flyingService.saveAndReturn(any())).thenReturn(saved);

        mockMvc.perform(post("/api/flights/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        Mockito.verify(flyingService).saveAndReturn(any());
    }

    @Test
    void testUpdateFlight_Exists() throws Exception {

        Flying existing = new Flying();
        existing.setId(7);

        FlyingDTO dto = new FlyingDTO();
        dto.setId(0);

        Flying updated = new Flying();
        updated.setId(7);

        Mockito.when(flyingService.findById(7)).thenReturn(Optional.of(existing));
        Mockito.when(flyingService.saveAndReturn(any())).thenReturn(updated);

        mockMvc.perform(put("/api/flights/api/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));

        Mockito.verify(flyingService).findById(7);
        Mockito.verify(flyingService).saveAndReturn(any());
    }

    @Test
    void testUpdateFlight_NotExists() throws Exception {

        Mockito.when(flyingService.findById(99)).thenReturn(Optional.empty());

        FlyingDTO dto = new FlyingDTO();
        dto.setId(0);

        mockMvc.perform(put("/api/flights/api/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());

        Mockito.verify(flyingService).findById(99);
        Mockito.verify(flyingService, Mockito.never()).saveAndReturn(any());
    }

    @Test
    void testDeleteFlight() throws Exception {

        mockMvc.perform(delete("/api/flights/api/12"))
                .andExpect(status().isNoContent());

        Mockito.verify(flyingService).deleteById(12);
    }
}