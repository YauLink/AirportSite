package com.myapp.Airports.controller.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.Airports.controller.rest.RestFilterController;
import com.myapp.Airports.model.Airport;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.view.api.IAirportsView;
import com.myapp.Airports.view.api.IFlyingsView;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestFilterController.class)
public class RestFilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAirportsView airportsView;

    @MockBean
    private IFlyingsView flyingsView;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFilter_NoParams() throws Exception {
        Mockito.when(airportsView.getAll())
                .thenReturn(List.of(new Airport("AAA", "Airport A")));

        mockMvc.perform(get("/api/filters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airports[0].code").value("AAA"))
                .andExpect(jsonPath("$.flights").doesNotExist())
                .andExpect(jsonPath("$.totalCount").doesNotExist());

        Mockito.verify(airportsView).getAll();
        Mockito.verifyNoInteractions(flyingsView);
    }

    @Test
    void testFilter_AirportOut() throws Exception {

        Mockito.when(airportsView.getAll())
                .thenReturn(List.of(new Airport("AAA", "Airport A")));

        Flying f = new Flying();
        f.setId(1);

        Mockito.when(flyingsView.getList(any())).thenReturn(List.of(f));
        Mockito.when(flyingsView.count(any())).thenReturn(1L);

        mockMvc.perform(get("/api/filters")
                        .param("airport_out", "AAA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airports[0].code").value("AAA"))
                .andExpect(jsonPath("$.flights[0].id").value(1))
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.currentPage").value(1));
    }

    @Test
    void testFilter_AirportIn() throws Exception {

        Mockito.when(airportsView.getAll())
                .thenReturn(List.of(new Airport("BBB", "Airport B")));

        Flying f = new Flying();
        f.setId(2);

        Mockito.when(flyingsView.getList(any())).thenReturn(List.of(f));
        Mockito.when(flyingsView.count(any())).thenReturn(5L);

        mockMvc.perform(get("/api/filters")
                        .param("airport_in", "BBB"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airports[0].code").value("BBB"))
                .andExpect(jsonPath("$.flights[0].id").value(2))
                .andExpect(jsonPath("$.totalCount").value(5))
                .andExpect(jsonPath("$.currentPage").value(1));
    }

    @Test
    void testFilter_BothParams_Page3() throws Exception {

        Mockito.when(airportsView.getAll())
                .thenReturn(List.of(new Airport("AAA", "Airport A")));

        Flying f = new Flying();
        f.setId(10);

        Mockito.when(flyingsView.getList(any())).thenReturn(List.of(f));
        Mockito.when(flyingsView.count(any())).thenReturn(100L);

        mockMvc.perform(get("/api/filters")
                        .param("airport_out", "AAA")
                        .param("airport_in", "BBB")
                        .param("page", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flights[0].id").value(10))
                .andExpect(jsonPath("$.totalCount").value(100))
                .andExpect(jsonPath("$.currentPage").value(3));
    }

    @Test
    void testFilter_CreatesCorrectFilterObject() throws Exception {

        Mockito.when(airportsView.getAll()).thenReturn(List.of());
        Mockito.when(flyingsView.getList(any())).thenReturn(List.of());
        Mockito.when(flyingsView.count(any())).thenReturn(0L);

        mockMvc.perform(get("/api/filters")
                        .param("airport_out", "AAA")
                        .param("airport_in", "CCC")
                        .param("page", "4"))
                .andExpect(status().isOk());

        ArgumentCaptor<IFlyingsView.FlyingFilter> captor =
                ArgumentCaptor.forClass(IFlyingsView.FlyingFilter.class);

        Mockito.verify(flyingsView).getList(captor.capture());

        IFlyingsView.FlyingFilter filter = captor.getValue();

        assert filter.getAirportOut().equals("AAA");
        assert filter.getAirportIn().equals("CCC");
        assert filter.getPage() == 4;
    }
}
