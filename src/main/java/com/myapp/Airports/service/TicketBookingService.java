package com.myapp.Airports.service;

import com.myapp.Airports.model.*;
import com.myapp.Airports.storage.api.ITicketFlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TicketBookingService {

    private final TicketService ticketService;
    private final BookingService bookingService;
    private final FlyingService flyingService;
    private final ITicketFlightRepository ticketFlightRepository;

    public TicketBookingService(TicketService ticketService,
                                BookingService bookingService,
                                FlyingService flyingService,
                                ITicketFlightRepository ticketFlightRepository) {
        this.ticketService = ticketService;
        this.bookingService = bookingService;
        this.flyingService = flyingService;
        this.ticketFlightRepository = ticketFlightRepository;
    }

    @Transactional
    public Booking createBookingWithTickets(Booking booking,
                                            String passengerId,
                                            String passengerName,
                                            String contactData,
                                            List<Integer> flightIds,
                                            List<String> fares,
                                            List<BigDecimal> amounts) {
        Booking savedBooking = bookingService.save(booking);
        createTicketsForBooking(
                savedBooking, passengerId, passengerName, contactData,
                flightIds, fares, amounts
        );
        return savedBooking;
    }

    @Transactional
    public void createTicketsForBooking(Booking booking,
                                        String passengerId,
                                        String passengerName,
                                        String contactData,
                                        List<Integer> flightIds,
                                        List<String> fares,
                                        List<BigDecimal> amounts) {

        if (flightIds.size() != fares.size() || flightIds.size() != amounts.size()) {
            throw new IllegalArgumentException("Each flight must have a fare and amount");
        }

        for (int i = 0; i < flightIds.size(); i++) {

            Integer flightId = flightIds.get(i);

            Flying flight = flyingService.findById(flightId);

            Ticket ticket = new Ticket();

            ticket.setTicketNo(generateTicketNo());
            ticket.setBooking(booking);
            ticket.setPassengerId(passengerId);
            ticket.setPassengerName(passengerName);
            ticket.setContactData(contactData);

            ticketService.save(ticket);

            TicketFlight tf = new TicketFlight();

            TicketFlightId tfId = new TicketFlightId();
            tfId.setTicketNo(ticket.getTicketNo());
            tfId.setFlightId(flight.getFlightId());

            tf.setId(tfId);
            tf.setTicket(ticket);
            tf.setFlight(flight);
            tf.setFareConditions(fares.get(i));
            tf.setAmount(amounts.get(i));
            ticketFlightRepository.save(tf);
        }
    }


    private String generateTicketNo() {
        return "T" + UUID.randomUUID().toString().replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}
