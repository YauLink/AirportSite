package com.myapp.Airports.service;

import com.myapp.Airports.model.*;
import com.myapp.Airports.storage.api.IBoardingPassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TicketBookingServiceTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private BookingService bookingService;

    @Mock
    private FlyingService flyingService;

    @Mock
    private IBoardingPassRepository boardingPassRepository;

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

        when(flyingService.findById(1)).thenReturn(Optional.of(flight1));
        when(flyingService.findById(2)).thenReturn(Optional.of(flight2));

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

        verify(boardingPassRepository, times(2)).save(any(BoardingPass.class));
    }

    @Test
    void shouldThrowExceptionWhenFlightNotFound() {

        Booking booking = new Booking();

        when(flyingService.findById(1)).thenReturn(Optional.empty());

        List<Integer> flightIds = List.of(1);
        List<String> fares = List.of("Economy");
        List<BigDecimal> amounts = List.of(new BigDecimal("100.00"));

        RuntimeException ex = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
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

        assert ex.getMessage().contains("Flight not found");

        verify(ticketService, never()).save(any());
        verify(boardingPassRepository, never()).save(any());
    }

    @Test
    void shouldCreateCorrectBoardingPassData() {

        Booking booking = new Booking();

        Flying flight = new Flying();
        flight.setFlightId(1);

        when(flyingService.findById(1)).thenReturn(Optional.of(flight));

        List<Integer> flightIds = List.of(1);
        List<String> fares = List.of("Economy");
        List<BigDecimal> amounts = List.of(new BigDecimal("100.00"));

        ArgumentCaptor<BoardingPass> captor = ArgumentCaptor.forClass(BoardingPass.class);

        ticketBookingService.createTicketsForBooking(
                booking,
                "P1",
                "John Doe",
                "{}",
                flightIds,
                fares,
                amounts
        );

        verify(boardingPassRepository).save(captor.capture());

        BoardingPass bp = captor.getValue();

        assert bp.getSeatNo().equals("AUTO-1");
        assert bp.getBoardingNo() == 1;
        assert bp.getFlightId() == 1;
    }
}