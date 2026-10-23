package com.myapp.Airports.controller.rest;

import com.myapp.Airports.dto.BookingDTO;
import com.myapp.Airports.mapper.BookingMapper;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.JwtUserPrincipal;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * REST controller responsible for managing flight bookings.
 * <p>
 * Provides endpoints for creating and retrieving booking information.
 * </p>
 */
@RestController
@RequestMapping("/api/bookings")
public class RestBookingController {

    private final BookingService bookingService;
    private final SeatService seatService;

    public RestBookingController(
            BookingService bookingService,
            SeatService seatService) {

        this.bookingService = bookingService;
        this.seatService = seatService;
    }

    @GetMapping
    public ResponseEntity<Page<BookingDTO>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            Authentication authentication) {

        Page<Booking> source = isAdmin(authentication)
                ? bookingService.findAll(page, size)
                : bookingService.findAllByPassengerId(currentUser(authentication), page, size);
        Page<BookingDTO> bookings = source
                        .map(BookingMapper::toDto);

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{ref}")
    public ResponseEntity<BookingDTO> getBooking(
            @PathVariable String ref,
            Authentication authentication) {

        Booking booking = isAdmin(authentication)
                ? bookingService.findByBookRef(ref)
                : bookingService.findByBookRefAndPassengerId(ref, currentUser(authentication));

        return ResponseEntity.ok(
                BookingMapper.toDto(booking)
        );
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(
            @RequestBody @Valid BookingDTO dto) {

        Booking saved =
                bookingService.save(
                        BookingMapper.toEntity(dto)
                );

        return ResponseEntity.ok(
                BookingMapper.toDto(saved)
        );
    }

    @PutMapping("/{ref}")
    public ResponseEntity<BookingDTO> updateBooking(
            @PathVariable String ref,
            @RequestBody @Valid BookingDTO dto) {

        Booking updated =
                bookingService.updateBookingAndReturn(
                        ref,
                        BookingMapper.toEntity(dto)
                );

        return ResponseEntity.ok(
                BookingMapper.toDto(updated)
        );
    }

    @DeleteMapping("/{ref}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable String ref) {

        bookingService.delete(ref);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ref}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable String ref) {

        bookingService.cancelBooking(ref);
        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
    }

    private String currentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUserPrincipal user)) {
            throw new org.springframework.security.access.AccessDeniedException("Authenticated user is required");
        }
        return String.valueOf(user.getUserId());
    }
}
