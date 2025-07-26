package com.myapp.Airports.controller.web;


import com.myapp.Airports.dto.BookingDTO;
import com.myapp.Airports.mapper.BookingMapper;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Seat;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.SeatService;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final SeatService seatService;

    public BookingController(BookingService bService, SeatService sService) {
        this.bookingService = bService;
        this.seatService = sService;
    }

    @GetMapping
    public List<BookingDTO> getAll() {
        return bookingService.findAll().stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookingDTO getOne(@PathVariable String id) {
        return BookingMapper.toDto(bookingService.findById(id));
    }

    @PostMapping
    public BookingDTO create(@RequestBody BookingDTO dto) {
        Booking booking = BookingMapper.toEntity(dto);
        return BookingMapper.toDto(bookingService.save(booking));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        bookingService.delete(id);
    }

    public String editBooking(@PathVariable("ref") String ref, Model model) {
        Booking booking = bookingService.findByBookRef(ref);
        model.addAttribute("booking", booking);
        //return "bookings/edit";
    }

    public String updateBooking(@PathVariable("ref") String ref, @ModelAttribute Booking booking) {
        bookingService.update(ref, booking);
        //return "redirect:/my/bookings";
    }

    public String cancelBooking(@PathVariable("ref") String ref) {
        bookingService.cancelBooking(ref);
        //return "redirect:/my/bookings";
    }

    public String showSeatSelection(@PathVariable String ref, Model model) {
        Booking booking = bookingService.findByBookRef(ref);
        List<Seat> availableSeats = seatService.findAvailableForFlight(booking);
        model.addAttribute("booking", booking);
        model.addAttribute("seats", availableSeats);
        return "bookings/seats";
    }

    public String assignSeat(@PathVariable String ref, @RequestParam("seatNo") String seatNo) {
        bookingService.assignSeat(ref, seatNo); // or use service to update TicketFlight
        //return "redirect:/my/bookings";
    }
}
