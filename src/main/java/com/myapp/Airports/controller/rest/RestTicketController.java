package com.myapp.Airports.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.Airports.dto.TicketDTO;
import com.myapp.Airports.mapper.TicketMapper;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller responsible for managing flight tickets.
 * <p>
 * Provides endpoints for creating and retrieving ticket information.
 * </p>
 */
@RestController
@RequestMapping("/api/tickets")
public class RestTicketController {

    private final TicketService service;

    public RestTicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> tickets = service.findAll()
                .stream()
                .map(TicketMapper::toDto)
                .toList();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{ticketNo}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable String ticketNo) {
        Ticket ticket = service.findById(ticketNo);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(TicketMapper.toDto(ticket));
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO dto) throws JsonProcessingException {
        Ticket ticket = TicketMapper.toEntity(dto);

        if (dto.getContactData() != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> wrapper = Map.of(
                    "note", Map.of("phone", dto.getContactData())
            );
            ticket.setContactData(mapper.writeValueAsString(wrapper));
        }

        Ticket saved = service.save(ticket);
        return ResponseEntity.ok(TicketMapper.toDto(saved));
    }

    @PutMapping("/{ticketNo}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable String ticketNo, @RequestBody TicketDTO dto) throws JsonProcessingException {
        Ticket ticket = service.findById(ticketNo);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        ticket.setPassengerName(dto.getPassengerName());

        if (dto.getContactData() != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> wrapper = Map.of(
                    "note", Map.of("phone", dto.getContactData())
            );
            ticket.setContactData(mapper.writeValueAsString(wrapper));
        }

        Ticket updated = service.save(ticket);
        return ResponseEntity.ok(TicketMapper.toDto(updated));
    }

    @DeleteMapping("/{ticketNo}")
    public ResponseEntity<Void> deleteTicket(@PathVariable String ticketNo) {
        service.delete(ticketNo);
        return ResponseEntity.noContent().build();
    }
}
