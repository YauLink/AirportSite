package com.myapp.Airports.service;

import com.myapp.Airports.model.Aircraft;
import com.myapp.Airports.storage.api.IAircraftRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AircraftServiceTest {

    @Mock
    private IAircraftRepository repository;

    @InjectMocks
    private AircraftService service;

    @Test
    void shouldReturnAllAircrafts() {
        List<Aircraft> aircrafts = List.of(new Aircraft(), new Aircraft());

        when(repository.findAll()).thenReturn(aircrafts);

        List<Aircraft> result = service.getAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void shouldReturnAircraftById() {
        Aircraft aircraft = new Aircraft();

        when(repository.findById("A1")).thenReturn(Optional.of(aircraft));

        Aircraft result = service.getById("A1");

        assertNotNull(result);
        verify(repository).findById("A1");
    }

    @Test
    void shouldThrowWhenAircraftNotFound() {
        when(repository.findById("A1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById("A1"));

        verify(repository).findById("A1");
    }

    @Test
    void shouldSaveAircraft() {
        Aircraft aircraft = new Aircraft();

        when(repository.save(aircraft)).thenReturn(aircraft);

        Aircraft result = service.save(aircraft);

        assertNotNull(result);
        verify(repository).save(aircraft);
    }

    @Test
    void shouldDeleteAircraft() {
        service.delete("A1");

        verify(repository).deleteById("A1");
    }
}