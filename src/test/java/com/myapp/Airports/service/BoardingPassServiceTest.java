package com.myapp.Airports.service;

import com.myapp.Airports.model.BoardingPass;
import com.myapp.Airports.storage.api.IBoardingPassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardingPassServiceTest {

    @Mock
    private IBoardingPassRepository repository;

    @InjectMocks
    private BoardingPassService service;

    @Test
    void shouldCreateBoardingPass() {
        BoardingPass bp = new BoardingPass();

        when(repository.save(bp)).thenReturn(bp);

        BoardingPass result = service.create(bp);

        assertNotNull(result);
        verify(repository).save(bp);
    }

    @Test
    void shouldThrowExceptionWhenSeatAlreadyTaken() {
        BoardingPass bp = new BoardingPass();

        when(repository.save(bp))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.create(bp)
        );

        assertEquals("Seat already taken for this flight", exception.getMessage());

        verify(repository).save(bp);
    }
}