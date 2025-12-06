package com.myapp.Airports.controller.rest;

import com.myapp.Airports.dto.BookingDTO;
import com.myapp.Airports.mapper.BookingMapper;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class RestBookingController {

    private final BookingService bookingService;
    private final SeatService seatService;

    public RestBookingController(BookingService bookingService, SeatService seatService) {
        this.bookingService = bookingService;
        this.seatService = seatService;
    }

    @GetMapping
    public ResponseEntity<Page<BookingDTO>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        Page<BookingDTO> bookings = bookingService.findAll(page, size)
                .map(BookingMapper::toDto);

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{ref}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable String ref) {
        Booking booking = bookingService.findByBookRef(ref);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookingMapper.toDto(booking));
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody @Valid BookingDTO dto) {
        Booking saved = bookingService.save(BookingMapper.toEntity(dto));
        return ResponseEntity.ok(BookingMapper.toDto(saved));
    }

    @PutMapping("/{ref}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable String ref, @RequestBody BookingDTO dto) {
        Booking updated = bookingService.updateBookingAndReturn(ref, BookingMapper.toEntity(dto));
        return ResponseEntity.ok(BookingMapper.toDto(updated));
    }

    @DeleteMapping("/{ref}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String ref) {
        bookingService.delete(ref);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ref}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable String ref) {
        bookingService.cancelBooking(ref);
        return ResponseEntity.noContent().build();
    }
}
