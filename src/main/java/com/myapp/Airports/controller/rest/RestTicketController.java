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

@RestController
@RequestMapping("/api/tickets")
public class RestTicketController {

    private final TicketService service;

    public RestTicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> tickets = service.findAll()
                .stream()
                .map(TicketMapper::toDto)
                .toList();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/api/{ticketNo}")
    @ResponseBody
    public ResponseEntity<TicketDTO> getTicket(@PathVariable String ticketNo) {
        Ticket ticket = service.findById(ticketNo);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(TicketMapper.toDto(ticket));
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO dto) throws JsonProcessingException {
        Ticket ticket = TicketMapper.toEntity(dto);
        if (dto.getContactData() != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> noteMap = Map.of("phone", dto.getContactData());
            Map<String, Object> wrapper = Map.of("note", noteMap);
            ticket.setContactData(mapper.writeValueAsString(wrapper));
        }
        Ticket saved = service.save(ticket);
        return ResponseEntity.ok(TicketMapper.toDto(saved));
    }

    @PutMapping("/api/{ticketNo}")
    @ResponseBody
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable String ticketNo, @RequestBody TicketDTO dto) throws JsonProcessingException {
        Ticket ticket = service.findById(ticketNo);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        ticket.setPassengerName(dto.getPassengerName());
        if (dto.getContactData() != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> noteMap = Map.of("phone", dto.getContactData());
            Map<String, Object> wrapper = Map.of("note", noteMap);
            ticket.setContactData(mapper.writeValueAsString(wrapper));
        }

        Ticket updated = service.save(ticket);
        return ResponseEntity.ok(TicketMapper.toDto(updated));
    }

    @DeleteMapping("/api/{ticketNo}")
    @ResponseBody
    public ResponseEntity<Void> deleteTicket(@PathVariable String ticketNo) {
        service.delete(ticketNo);
        return ResponseEntity.noContent().build();
    }
}
