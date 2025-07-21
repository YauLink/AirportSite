package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.TicketDTO;
import com.myapp.Airports.dto.TicketMapper;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    public List<TicketDTO> getAll() {
        return service.findAll().stream()
                .map(TicketMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{ticketNo}")
    public TicketDTO getById(@PathVariable String ticketNo) {
        return TicketMapper.toDto(service.findById(ticketNo));
    }

    @PostMapping
    public TicketDTO create(@RequestBody TicketDTO dto) {
        Booking booking = service.getBooking(dto.getBookRef());
        Ticket ticket = TicketMapper.toEntity(dto, booking);
        return TicketMapper.toDto(service.save(ticket));
    }

    @DeleteMapping("/{ticketNo}")
    public void delete(@PathVariable String ticketNo) {
        service.delete(ticketNo);
    }
}
