package com.myapp.Airports.view;

import com.myapp.Airports.model.Flying;
import com.myapp.Airports.storage.api.IFlyingsRepository;
import com.myapp.Airports.view.api.IFlyingsView.FlyingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FlyingsViewTest {

    private IFlyingsRepository repository;
    private FlyingsView view;

    @BeforeEach
    void setup() {
        repository = mock(IFlyingsRepository.class);
        view = new FlyingsView(repository);
    }

    @Test
    void count_ShouldCallRepositoryWithCorrectParams() {
        FlyingFilter filter = new FlyingFilter("JFK", "LAX", 1);

        when(repository.countByDepartureAirportAndArrivalAirport("JFK", "LAX"))
                .thenReturn(5L);

        long count = view.count(filter);

        assertEquals(5L, count);

        verify(repository).countByDepartureAirportAndArrivalAirport("JFK", "LAX");
    }
    @Test
    void getList_ShouldCallRepositoryWithCorrectParams() {
        FlyingFilter filter = new FlyingFilter("JFK", "LAX", 1);
        List<Flying> expectedList = List.of(new Flying(), new Flying());

        Pageable pageable = PageRequest.of(0, 20); // page = filter.page - 1

        when(repository.findAllByDepartureAirportAndArrivalAirport(
                "JFK", "LAX", pageable
        )).thenReturn(expectedList);

        List<Flying> result = view.getList(filter);

        assertEquals(expectedList, result);

        verify(repository).findAllByDepartureAirportAndArrivalAirport("JFK", "LAX", pageable);
    }

    @Test
    void get_ShouldCallGetById() {
        Flying expected = new Flying();
        when(repository.getById(42)).thenReturn(expected);

        Flying result = view.get(42);

        assertEquals(expected, result);
        verify(repository).getById(42);
    }

    @Test
    void save_ShouldCallSave() {
        Flying flying = new Flying();
        view.save(flying);

        verify(repository).save(flying);
    }
}