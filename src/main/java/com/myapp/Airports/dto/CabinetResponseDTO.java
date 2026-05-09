package com.myapp.Airports.dto;

import com.myapp.Airports.model.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CabinetResponseDTO {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be up to 100 characters")
    private String fullName;

    private List<Ticket> tickets;

    public CabinetResponseDTO(String fullName, List<Ticket> tickets) {
        this.fullName = fullName;
        this.tickets = tickets;
    }

    public String getFullName() {
        return fullName;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}
