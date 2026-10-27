package com.myapp.Airports.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CabinetResponseDTO {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be up to 100 characters")
    private String fullName;

    private List<CabinetTicketDTO> tickets;

    public CabinetResponseDTO(String fullName, List<CabinetTicketDTO> tickets) {
        this.fullName = fullName;
        this.tickets = tickets;
    }

    public String getFullName() {
        return fullName;
    }

    public List<CabinetTicketDTO> getTickets() {
        return tickets;
    }
}
