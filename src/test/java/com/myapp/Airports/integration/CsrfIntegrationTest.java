package com.myapp.Airports.integration;

import com.myapp.Airports.service.TicketService;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Exercises the production security filter chain rather than bypassing it in a MVC slice.
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class CsrfIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    void rejectsStateChangingRequestWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/tickets/T123/delete"))
                .andExpect(status().isForbidden());

        verify(ticketService, never()).delete(anyString());
    }

    @Test
    void acceptsStateChangingRequestWithValidCsrfToken() throws Exception {
        mockMvc.perform(post("/tickets/T123/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tickets/list"));

        verify(ticketService).delete("T123");
    }

    @Test
    void rejectsRequestWithInvalidCsrfToken() throws Exception {
        mockMvc.perform(
                        post("/tickets/T123/delete")
                                .with(csrf().useInvalidToken())
                )
                .andExpect(status().isForbidden());

        verify(ticketService, never()).delete(anyString());
    }

    @Test
    void doesNotCallServiceWhenCsrfValidationFails() throws Exception {
        mockMvc.perform(post("/tickets/T123/delete"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(ticketService);
    }

    @Test
    void passesCorrectTicketReferenceToService() throws Exception {
        mockMvc.perform(post("/tickets/ABC123/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tickets/list"));

        verify(ticketService).delete("ABC123");
    }

    @Test
    void authenticatedUserCanDeleteTicket() throws Exception {
        mockMvc.perform(post("/tickets/T123/delete").with(csrf()))
                .andExpect(status().is3xxRedirection());

        verify(ticketService).delete("T123");
    }
}
