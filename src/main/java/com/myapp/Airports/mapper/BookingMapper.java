package com.myapp.Airports.mapper;

import com.myapp.Airports.dto.BookingDTO;
import com.myapp.Airports.model.Booking;

public class BookingMapper {

    public static BookingDTO toDto(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookRef(booking.getBookRef());
        dto.setBookDate(booking.getBookDate());
        dto.setTotalAmount(booking.getTotalAmount());
        return dto;
    }

    public static Booking toEntity(BookingDTO dto) {
        Booking booking = new Booking();
        booking.setBookRef(dto.getBookRef());
        booking.setBookDate(dto.getBookDate());
        booking.setTotalAmount(dto.getTotalAmount());
        return booking;
    }
}
