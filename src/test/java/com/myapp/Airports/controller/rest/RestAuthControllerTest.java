package com.myapp.Airports.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.Airports.dto.AuthRequestDTO;
import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.dto.CabinetResponseDTO;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.model.JwtUserPrincipal;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

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

    private Authentication userAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                new JwtUserPrincipal(1L, "john", "John Doe", "USER"),
                null,
                List.of());
    }

    @Test
    @DisplayName("Should login successfully")
    void shouldLoginSuccessfully() throws Exception {

        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setUserId(1L);
        responseDTO.setFullName("John Doe");

        Mockito.when(authService.login(anyString(), anyString()))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthRequestDTO("john", "1234"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    @DisplayName("Should return unauthorized for invalid login")
    void shouldReturnBadRequestForInvalidLogin() throws Exception {

        Mockito.when(authService.login(anyString(), anyString()))
                .thenReturn(new AuthResponseDTO());

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthRequestDTO("wrong", "wrong"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    @DisplayName("Should return cabinet information")
    void shouldReturnCabinetInformation() throws Exception {

        Ticket ticket = new Ticket();

        Mockito.when(ticketService.findAllByUserId("1"))
                .thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/user/cabinet")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(userAuthentication())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.tickets").isArray());
    }

    @Test
    @DisplayName("Should return unauthorized when session is missing")
    void shouldReturnUnauthorizedWhenSessionMissing() throws Exception {

        mockMvc.perform(get("/api/user/cabinet"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHENTICATED"))
                .andExpect(jsonPath("$.message").value("User is not authenticated"));
    }

    @Test
    @DisplayName("Should logout successfully")
    void shouldLogoutSuccessfully() throws Exception {

        mockMvc.perform(post("/api/user/logout")
                        .session(new MockHttpSession()))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out"))
                .andExpect(cookie().maxAge("AIRPORTS_JWT", 0));
    }
}
