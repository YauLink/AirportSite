package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.TicketDTO;
import com.myapp.Airports.mapper.TicketMapper;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    public String showTicketList(Model model) {
        model.addAttribute("tickets", service.findAll().stream()
                .map(TicketMapper::toDto)
                .collect(Collectors.toList()));
        return "tickets/list";
    }

    @GetMapping("/{ticketNo}/edit")
    public String showEditForm(@PathVariable String ticketNo, Model model) {
        model.addAttribute("ticket", TicketMapper.toDto(service.findById(ticketNo)));
        return "tickets/edit";
    }

    @PostMapping("/{ticketNo}/update")
    public String update(@PathVariable String ticketNo, TicketDTO dto) {
        Booking booking = service.getBooking(dto.getBookRef());
        Ticket ticket = TicketMapper.toEntity(dto, booking);
        service.save(ticket);
        return "redirect:/tickets/list";
    }

    @PostMapping("/{ticketNo}/delete")
    public String delete(@PathVariable String ticketNo) {
        service.delete(ticketNo);
        return "redirect:/tickets/list";
    }
}
