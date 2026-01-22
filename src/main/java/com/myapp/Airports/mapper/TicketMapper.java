package com.myapp.Airports.mapper;

import com.myapp.Airports.dto.TicketDTO;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Ticket;

public class TicketMapper {

    public static TicketDTO toDto (Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setTicketNo(ticket.getTicketNo());
        dto.setBooking(ticket.getBooking());
        dto.setPassengerId(ticket.getPassengerId());
        dto.setPassengerName(ticket.getPassengerName());
        dto.setContactData(ticket.getContactData());
        return dto;
    }

    public static Ticket toEntity(TicketDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setTicketNo(dto.getTicketNo());
        ticket.setBooking(dto.getBooking());
        ticket.setPassengerId(dto.getPassengerId());
        ticket.setPassengerName(dto.getPassengerName());
        ticket.setContactData(dto.getContactData());
        return ticket;
    }
}
