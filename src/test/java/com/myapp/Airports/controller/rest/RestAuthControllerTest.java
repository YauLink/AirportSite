package com.myapp.Airports.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.dto.CabinetResponseDTO;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestAuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private TicketService ticketService;

    @Test
    @DisplayName("Should login successfully")
    void shouldLoginSuccessfully() throws Exception {

        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setUserId(1L);
        responseDTO.setFullName("John Doe");

        Mockito.when(authService.login(anyString(), anyString()))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/user/login")
                        .param("username", "john")
                        .param("password", "1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    @DisplayName("Should return bad request for invalid login")
    void shouldReturnBadRequestForInvalidLogin() throws Exception {

        Mockito.when(authService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/user/login")
                        .param("username", "wrong")
                        .param("password", "wrong"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    @DisplayName("Should return cabinet information")
    void shouldReturnCabinetInformation() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("USER_ID", 1L);
        session.setAttribute("USER_NAME", "John Doe");

        Ticket ticket = new Ticket();

        Mockito.when(ticketService.findAllByUserId("1"))
                .thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/user/cabinet")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.tickets").isArray());
    }

    @Test
    @DisplayName("Should return unauthorized when session is missing")
    void shouldReturnUnauthorizedWhenSessionMissing() throws Exception {

        mockMvc.perform(get("/api/user/cabinet"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));
    }

    @Test
    @DisplayName("Should logout successfully")
    void shouldLogoutSuccessfully() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("USER_ID", 1L);

        mockMvc.perform(post("/api/user/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out"));
    }
}