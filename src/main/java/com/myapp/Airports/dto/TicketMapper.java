package com.myapp.Airports.dto;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;

public class TicketMapper {

    public static TicketDTO toDto (Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setTicketNo(ticket.getTicketNo());
        dto.setBookRef(ticket.getBooking().getBookRef());
        dto.setPassengerId(ticket.getPassengerId());
        dto.setPassengerName(ticket.getPassengerName());
        dto.setContactData(ticket.getContactData());
        return dto;
    }

    public static Ticket toEntity(TicketDTO dto, Booking booking) {
        Ticket ticket = new Ticket();
        ticket.setTicketNo(dto.getTicketNo());
        ticket.setBooking(booking);
        ticket.setPassengerId(dto.getPassengerId());
        ticket.setPassengerName(dto.getPassengerName());
        ticket.setContactData(dto.getContactData());
        return ticket;
    }
}
