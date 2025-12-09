package com.myapp.Airports.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.myapp.Airports.model.Flying;
import com.myapp.Airports.storage.api.IFlyingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

class FlyingServiceTest {

    @InjectMocks
    private FlyingService flyingService;

    @Mock
    private IFlyingsRepository flyingRepository;

    private Flying flight1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        flight1 = new Flying();
        flight1.setId(1);
        flight1.setFlightNo("FL123");
    }

    @Test
    void findAll_ShouldReturnFlights() {
        when(flyingRepository.findAll()).thenReturn(List.of(flight1));

        List<Flying> flights = flyingService.findAll();

        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("FL123", flights.get(0).getFlightNo());
        verify(flyingRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnFlight() {
        when(flyingRepository.findById(1)).thenReturn(Optional.of(flight1));

        Optional<Flying> flight = flyingService.findById(1);

        assertTrue(flight.isPresent());
        assertEquals("FL123", flight.get().getFlightNo());
        verify(flyingRepository, times(1)).findById(1);
    }

    @Test
    void save_ShouldReturnSavedFlight() {
        when(flyingRepository.save(flight1)).thenReturn(flight1);

        Flying saved = flyingService.saveAndReturn(flight1);

        assertNotNull(saved);
        assertEquals("FL123", saved.getFlightNo());
        verify(flyingRepository, times(1)).save(flight1);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(flyingRepository).deleteById(1);

        flyingService.deleteById(1);

        verify(flyingRepository, times(1)).deleteById(1);
    }
}
