package com.myapp.Airports.controller.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.Airports.dto.TicketDTO;
import com.myapp.Airports.mapper.TicketMapper;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    /* ---------------- MVC (Thymeleaf) ---------------- */

    @GetMapping
    public String showTicketList(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 20, amount = 100;
        Page<Ticket> ticketPage = service.getAllTickets(amount, pageSize);

        model.addAttribute("tickets", ticketPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketPage.getTotalPages());

        return "tickets/list";
    }

    @GetMapping("/{ticketNo}/edit")
    public String showEditForm(@PathVariable String ticketNo, Model model) throws JsonProcessingException {
        Ticket ticket = service.findById(ticketNo);

        String contactDisplay = "";
        if (ticket.getContactData() != null && !ticket.getContactData().isBlank()) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(ticket.getContactData(), Map.class);

            if (map.containsKey("note") && map.get("note") instanceof Map) {
                map = (Map<String, Object>) map.get("note");
            }

            contactDisplay = map.entrySet().stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining(", "));
        }

        TicketDTO dto = TicketMapper.toDto(ticket);
        dto.setContactData(contactDisplay);

        model.addAttribute("ticket", dto);
        return "tickets/edit";
    }

    @PostMapping("/{ticketNo}/update")
    public String update(@PathVariable String ticketNo, TicketDTO dto) throws JsonProcessingException {
        Ticket ticket = service.findById(ticketNo);
        if (ticket == null) throw new RuntimeException("Ticket not found");

        ticket.setPassengerName(dto.getPassengerName());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> noteMap = Map.of("phone", dto.getContactData());
        Map<String, Object> wrapper = Map.of("note", noteMap);
        String json = mapper.writeValueAsString(wrapper);

        ticket.setContactData(json);

        service.save(ticket);
        return "redirect:/tickets/list";
    }

    @PostMapping("/{ticketNo}/delete")
    public String delete(@PathVariable String ticketNo) {
        service.delete(ticketNo);
        return "redirect:/tickets/list";
    }

    /* ---------------- REST API ENDPOINTS ---------------- */

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
