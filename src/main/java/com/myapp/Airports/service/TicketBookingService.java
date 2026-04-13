package com.myapp.Airports.service;

import com.myapp.Airports.model.Booking;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.model.TicketFlight;
import com.myapp.Airports.model.TicketFlightId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TicketBookingService {

    private final TicketService ticketService;
    private final BookingService bookingService;
    private final FlyingService flyingService;

    public TicketBookingService(TicketService ticketService,
                                BookingService bookingService,
                                FlyingService flyingService) {
        this.ticketService = ticketService;
        this.bookingService = bookingService;
        this.flyingService = flyingService;
    }

    /**
     * Create tickets for a booking with associated flights.
     *
     * @param booking     Booking to create tickets for
     * @param passengerId Passenger ID
     * @param passengerName Passenger full name
     * @param contactData JSON string with contact info
     * @param flightIds   List of flight IDs
     * @param fares       List of fare conditions corresponding to flights
     * @param amounts     List of amounts corresponding to flights
     */
    public void createTicketsForBooking(Booking booking,
                                        String passengerId,
                                        String passengerName,
                                        String contactData,
                                        List<Integer> flightIds,
                                        List<String> fares,
                                        List<BigDecimal> amounts) {

        for (int i = 0; i < flightIds.size(); i++) {
            
            Ticket ticket = new Ticket();
            ticket.setTicketNo(generateTicketNo());
            ticket.setBooking(booking);
            ticket.setPassengerId(passengerId);
            ticket.setPassengerName(passengerName);
            ticket.setContactData(contactData);

            ticketService.save(ticket);

            Integer flightId = flightIds.get(i);

            Flying flight = flyingService.findById(flightIds.get(flightId))
                    .orElseThrow(() -> new RuntimeException("Flight not found: " + flightIds.get(flightId)));

            TicketFlight tf = new TicketFlight();
            TicketFlightId id = new TicketFlightId();
            id.setTicketNo(ticket.getTicketNo());
            id.setFlightId(flight.getFlightId());
            tf.setId(id);

            tf.setTicket(ticket);
            tf.setFlight(flight);
            tf.setFareConditions(fares.get(i));
            tf.setAmount(amounts.get(i));

            bookingService.assignSeat(booking.getBookRef(), null); // optional seat logic
            bookingService.save(booking); // ensures cascading saves TicketFlight if needed
        }
    }
    /**
     * Simple unique ticket number generator.
     * Replace with your own implementation if needed.
     */
    private String generateTicketNo() {
        return "T" + System.currentTimeMillis();
    }
}