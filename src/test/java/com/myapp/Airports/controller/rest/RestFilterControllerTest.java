package com.myapp.Airports.controller.rest;

import com.myapp.Airports.model.Airport;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.view.api.IAirportsView;
import com.myapp.Airports.view.api.IFlyingsView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestFilterController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestFilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAirportsView airportsView;

    @MockBean
    private IFlyingsView flyingsView;

    private Airport airportA;
    private Flying flight1;

    @BeforeEach
    void setup() {
        airportA = new Airport("AAA", "Airport A");
        airportA.setCity("CityA");
        airportA.setTimezone("UTC+1");

        flight1 = new Flying();
        flight1.setFlightId(1);
        flight1.setFlightNo("AB123");
        flight1.setDepartureAirport("AAA");
        flight1.setArrivalAirport("BBB");
        flight1.setStatus("SCHEDULED");
        flight1.setScheduledDeparture(LocalDateTime.of(2025, 1, 1, 10, 0));
        flight1.setScheduledArrival(LocalDateTime.of(2025, 1, 1, 12, 0));
        flight1.setAircraftCode("A320");
    }

    @Test
    void testFilterFlights_NoFilters() throws Exception {
        when(airportsView.getAll()).thenReturn(List.of(airportA));

        mockMvc.perform(get("/api/filters")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airports[0].code").value("AAA"))
                .andExpect(jsonPath("$.flights").doesNotExist())
                .andExpect(jsonPath("$.totalCount").doesNotExist())
                .andExpect(jsonPath("$.currentPage").doesNotExist());

        verify(airportsView, times(1)).getAll();
        verifyNoInteractions(flyingsView);
    }

    @Test
    void testFilterFlights_WithFilters() throws Exception {
        when(airportsView.getAll()).thenReturn(List.of(airportA));
        when(flyingsView.getList(any(IFlyingsView.FlyingFilter.class))).thenReturn(List.of(flight1));
        when(flyingsView.count(any(IFlyingsView.FlyingFilter.class))).thenReturn(25L);

        mockMvc.perform(
                        get("/api/filters")
                                .param("airport_out", "AAA")
                                .param("airport_in", "BBB")
                                .param("page", "1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                // airports
                .andExpect(jsonPath("$.airports[0].code").value("AAA"))
                // flights
                .andExpect(jsonPath("$.flights[0].flightId").value(1))
                .andExpect(jsonPath("$.flights[0].flightNo").value("AB123"))
                .andExpect(jsonPath("$.flights[0].departureAirport").value("AAA"))
                .andExpect(jsonPath("$.flights[0].arrivalAirport").value("BBB"))
                .andExpect(jsonPath("$.flights[0].status").value("SCHEDULED"))
                .andExpect(jsonPath("$.flights[0].aircraftCode").value("A320"))
                // count & paging
                .andExpect(jsonPath("$.totalCount").value(25))
                .andExpect(jsonPath("$.currentPage").value(1));

        // VERIFY FILTER IS BUILT CORRECTLY
        ArgumentCaptor<IFlyingsView.FlyingFilter> filterCaptor = ArgumentCaptor.forClass(IFlyingsView.FlyingFilter.class);
        verify(flyingsView).getList(filterCaptor.capture());

        IFlyingsView.FlyingFilter filter = filterCaptor.getValue();
        assert filter.getAirportOut().equals("AAA");
        assert filter.getAirportIn().equals("BBB");
        assert filter.getPage() == 1;
    }

    @Test
    void testFilterFlights_EmptyParams_NotApplied() throws Exception {
        when(airportsView.getAll()).thenReturn(List.of(airportA));

        mockMvc.perform(
                        get("/api/filters")
                                .param("airport_out", "")
                                .param("airport_in", "")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airports[0].code").value("AAA"))
                .andExpect(jsonPath("$.flights").doesNotExist());

        verify(airportsView, times(1)).getAll();
        verifyNoInteractions(flyingsView);
    }

    @Test
    void testFilterFlights_OnlyDepartureProvided() throws Exception {
        when(airportsView.getAll()).thenReturn(List.of(airportA));
        when(flyingsView.getList(any())).thenReturn(List.of(flight1));
        when(flyingsView.count(any())).thenReturn(1L);

        mockMvc.perform(
                        get("/api/filters")
                                .param("airport_out", "AAA")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flights[0].departureAirport").value("AAA"))
                .andExpect(jsonPath("$.totalCount").value(1));

        verify(flyingsView, times(1)).getList(any());
        verify(flyingsView, times(1)).count(any());
    }
}
