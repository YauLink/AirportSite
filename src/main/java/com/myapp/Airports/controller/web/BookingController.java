package com.myapp.Airports.controller.web;


import com.myapp.Airports.dto.BookingDTO;
import com.myapp.Airports.mapper.BookingMapper;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookingDTO> getAll() {
        return service.findAll().stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookingDTO getOne(@PathVariable String id) {
        return BookingMapper.toDto(service.findById(id));
    }

    @PostMapping
    public BookingDTO create(@RequestBody BookingDTO dto) {
        Booking booking = BookingMapper.toEntity(dto);
        return BookingMapper.toDto(service.save(booking));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
