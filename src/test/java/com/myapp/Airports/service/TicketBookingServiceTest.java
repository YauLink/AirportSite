package com.myapp.Airports.service;

import com.myapp.Airports.model.*;
import com.myapp.Airports.storage.api.ITicketFlightRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketBookingServiceTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private BookingService bookingService;

    @Mock
    private FlyingService flyingService;

    @Mock
    private ITicketFlightRepository ticketFlightRepository;

    @InjectMocks
    private TicketBookingService ticketBookingService;

    @Test
    void shouldCreateTicketsAndBoardingPasses() {

        Booking booking = new Booking();
        booking.setBookRef("BKG-1");

        Flying flight1 = new Flying();
        flight1.setFlightId(1);

        Flying flight2 = new Flying();
        flight2.setFlightId(2);

        when(flyingService.findById(1)).thenReturn(flight1);
        when(flyingService.findById(2)).thenReturn(flight2);

        List<Integer> flightIds = List.of(1, 2);
        List<String> fares = List.of("Economy", "Business");
        List<BigDecimal> amounts = List.of(
                new BigDecimal("100.00"),
                new BigDecimal("200.00")
        );

        ticketBookingService.createTicketsForBooking(
                booking,
                "P1",
                "John Doe",
                "{}",
                flightIds,
                fares,
                amounts
        );

        verify(ticketService, times(2)).save(any(Ticket.class));

        verify(ticketFlightRepository, times(2)).save(any(TicketFlight.class));
    }

    @Test
    void shouldRejectMismatchedFlightData() {

        Booking booking = new Booking();

        List<Integer> flightIds = List.of(1, 2);
        List<String> fares = List.of("Economy");
        List<BigDecimal> amounts = List.of(new BigDecimal("100.00"));

        assertThrows(
                IllegalArgumentException.class,
                () -> ticketBookingService.createTicketsForBooking(
                        booking,
                        "P1",
                        "John Doe",
                        "{}",
                        flightIds,
                        fares,
                        amounts
                )
        );

        verify(ticketService, never()).save(any());
        verifyNoInteractions(ticketFlightRepository);
    }

    @Test
    void shouldCreateCorrectBoardingPassData() {

        Booking booking = new Booking();

        Flying flight = new Flying();
        flight.setFlightId(1);

        when(flyingService.findById(1)).thenReturn(flight);

        List<Integer> flightIds = List.of(1);
        List<String> fares = List.of("Economy");
        List<BigDecimal> amounts = List.of(new BigDecimal("100.00"));

        ArgumentCaptor<TicketFlight> captor = ArgumentCaptor.forClass(TicketFlight.class);

        ticketBookingService.createTicketsForBooking(
                booking,
                "P1",
                "John Doe",
                "{}",
                flightIds,
                fares,
                amounts
        );

        verify(ticketFlightRepository).save(captor.capture());

        TicketFlight ticketFlight = captor.getValue();

        assert ticketFlight.getFareConditions().equals("Economy");
        assert ticketFlight.getAmount().equals(new BigDecimal("100.00"));
    }
}
