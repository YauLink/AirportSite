package com.myapp.Airports.mapper;

import com.myapp.Airports.dto.TicketDTO;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketMapperTest {

    @Test
    void toDtoUsesBookingReferenceInsteadOfEntity() {
        Booking booking = new Booking();
        booking.setBookRef("B001");

        Ticket ticket = new Ticket();
        ticket.setTicketNo("T001");
        ticket.setBooking(booking);

        TicketDTO dto = TicketMapper.toDto(ticket);

        assertEquals("B001", dto.getBookingRef());
    }

    @Test
    void toEntityCreatesBookingReferenceOnly() {
        TicketDTO dto = new TicketDTO("T001", "B001", "P001", "Passenger", "{}");

        Ticket ticket = TicketMapper.toEntity(dto);

        assertEquals("B001", ticket.getBooking().getBookRef());
        assertTrue(ticket.getBooking().getTickets().isEmpty());
    }
}
