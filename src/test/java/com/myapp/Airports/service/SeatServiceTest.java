package com.myapp.Airports.service;

import com.myapp.Airports.model.Aircraft;
import com.myapp.Airports.model.Seat;
import com.myapp.Airports.model.SeatId;
import com.myapp.Airports.storage.api.ISeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SeatServiceTest {

    @InjectMocks
    private SeatService seatService;

    @Mock
    private ISeatRepository seatRepository;

    private Seat seat;
    private SeatId seatId;
    private Aircraft aircraft;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        seatId = new SeatId("1A", "A320");

        aircraft = new Aircraft();
        aircraft.setAircraftCode("A320");

        seat = new Seat();
        seat.setId(seatId);
        seat.setAircraft(aircraft);
        seat.setFareConditions("Economy");
    }

    @Test
    void getAll_ShouldReturnSeats() {
        when(seatRepository.findAll()).thenReturn(List.of(seat));

        List<Seat> seats = seatService.getAll();

        assertNotNull(seats);
        assertEquals(1, seats.size());
        assertEquals("1A", seats.get(0).getId().getSeatNo());
        assertEquals("A320", seats.get(0).getAircraft().getAircraftCode());

        verify(seatRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnSeat() {
        when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));

        Seat result = seatService.getById(seatId);

        assertNotNull(result);
        assertEquals("1A", result.getId().getSeatNo());
        assertEquals("Economy", result.getFareConditions());

        verify(seatRepository, times(1)).findById(seatId);
    }

    @Test
    void save_ShouldReturnSavedSeat() {
        when(seatRepository.save(seat)).thenReturn(seat);

        Seat saved = seatService.save(seat);

        assertNotNull(saved);
        assertEquals("A320", saved.getAircraft().getAircraftCode());

        verify(seatRepository, times(1)).save(seat);
    }

    @Test
    void delete_ShouldCallRepository() {
        doNothing().when(seatRepository).deleteById(seatId);

        seatService.delete(seatId);

        verify(seatRepository, times(1)).deleteById(seatId);
    }

    @Test
    void findAvailableForFlight_ShouldReturnSeats() {
        when(seatRepository.findAvailableSeatsByFlightId(1))
                .thenReturn(List.of(seat));

        List<Seat> seats = seatService.findAvailableForFlight(1);

        assertNotNull(seats);
        assertEquals(1, seats.size());
        assertEquals("1A", seats.get(0).getId().getSeatNo());

        verify(seatRepository, times(1))
                .findAvailableSeatsByFlightId(1);
    }
}
