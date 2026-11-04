package com.myapp.Airports.service;

import com.myapp.Airports.model.*;
import com.myapp.Airports.storage.api.ITicketFlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Coordinates creation of bookings, tickets, and ticket-flight associations.
 * <p>
 * Uses a transaction so all records for a booking are created together.
 * </p>
 */
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

        List<Flying> flights = flyingService.findAllByIds(flightIds.stream().distinct().toList());
        Map<Integer, Flying> flightsById = flights.stream()
                .collect(Collectors.toMap(Flying::getFlightId, Function.identity()));
        List<Ticket> tickets = new java.util.ArrayList<>(flightIds.size());
        List<TicketFlight> ticketFlights = new java.util.ArrayList<>(flightIds.size());

        for (int i = 0; i < flightIds.size(); i++) {

            Integer flightId = flightIds.get(i);
            Flying flight = flightsById.get(flightId);
            if (flight == null) {
                throw new IllegalArgumentException("One or more flights were not found");
            }

            Ticket ticket = new Ticket();

            ticket.setTicketNo(generateTicketNo());
            ticket.setBooking(booking);
            ticket.setPassengerId(passengerId);
            ticket.setPassengerName(passengerName);
            ticket.setContactData(contactData);

            tickets.add(ticket);

            TicketFlight tf = new TicketFlight();

            TicketFlightId tfId = new TicketFlightId();
            tfId.setTicketNo(ticket.getTicketNo());
            tfId.setFlightId(flight.getFlightId());

            tf.setId(tfId);
            tf.setTicket(ticket);
            tf.setFlight(flight);
            tf.setFareConditions(fares.get(i));
            tf.setAmount(amounts.get(i));
            ticketFlights.add(tf);
        }

        ticketService.saveAll(tickets);
        ticketFlightRepository.saveAll(ticketFlights);
    }


    private String generateTicketNo() {
        return "T" + UUID.randomUUID().toString().replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}
