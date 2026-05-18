package com.myapp.Airports.service;

import com.myapp.Airports.model.*;
import com.myapp.Airports.storage.api.IBoardingPassRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TicketBookingService {

    private final TicketService ticketService;
    private final BookingService bookingService;
    private final FlyingService flyingService;
    private final IBoardingPassRepository boardingPassRepository;

    public TicketBookingService(TicketService ticketService,
                                BookingService bookingService,
                                FlyingService flyingService,
                                IBoardingPassRepository boardingPassRepository) {
        this.ticketService = ticketService;
        this.bookingService = bookingService;
        this.flyingService = flyingService;
        this.boardingPassRepository = boardingPassRepository;
    }

    public void createTicketsForBooking(Booking booking,
                                        String passengerId,
                                        String passengerName,
                                        String contactData,
                                        List<Integer> flightIds,
                                        List<String> fares,
                                        List<BigDecimal> amounts) {

        try {

            for (int i = 0; i < flightIds.size(); i++) {

                Integer flightId = flightIds.get(i);

                Flying flight = flyingService.findById(flightId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Flight not found: " + flightId
                                ));

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

                BoardingPass bp = new BoardingPass();

                BoardingPassId bpId = new BoardingPassId();
                bpId.setTicketNo(ticket.getTicketNo());
                bpId.setFlightId(flight.getFlightId());

                bp.setId(bpId);
                bp.setTicketNo(ticket.getTicketNo());
                bp.setFLightId(flight.getFlightId());

                bp.setSeatNo("AUTO-" + (i + 1));
                bp.setBoardingNo(i + 1);

                boardingPassRepository.save(bp);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create tickets for booking: " + booking.getBookRef(),
                    e
            );
        }
    }

    private String generateTicketNo() {
        return "T" + System.currentTimeMillis();
    }
}