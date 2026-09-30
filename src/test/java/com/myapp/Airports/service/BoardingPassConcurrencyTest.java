package com.myapp.Airports.service;

import com.myapp.Airports.model.BoardingPass;
import com.myapp.Airports.storage.api.IBoardingPassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardingPassConcurrencyTest {

    @Mock
    private IBoardingPassRepository repository;

    @Test
    void concurrentClaimsMustTurnTheDatabaseConflictIntoSeatUnavailable() throws Exception {
        CountDownLatch bothReachedInsert = new CountDownLatch(2);
        when(repository.saveAndFlush(any(BoardingPass.class))).thenAnswer(invocation -> {
            bothReachedInsert.countDown();
            bothReachedInsert.await();
            throw new DataIntegrityViolationException("boarding_passes_flight_id_seat_no_key");
        });

        BoardingPassService service = new BoardingPassService(repository);
        BoardingPass first = pass("T1");
        BoardingPass second = pass("T2");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<Throwable> result1 = executor.submit(() -> failureOf(() -> service.create(first)));
            Future<Throwable> result2 = executor.submit(() -> failureOf(() -> service.create(second)));

            assertEquals("Seat already taken for this flight", result1.get().getMessage());
            assertEquals("Seat already taken for this flight", result2.get().getMessage());
        } finally {
            executor.shutdownNow();
        }

        verify(repository, times(2)).saveAndFlush(any(BoardingPass.class));
    }

    private Throwable failureOf(Runnable operation) {
        return assertThrows(RuntimeException.class, operation);
    }

    private BoardingPass pass(String ticketNo) {
        BoardingPass pass = new BoardingPass();
        pass.setTicketNo(ticketNo);
        pass.setFLightId(42);
        pass.setBoardingNo(1);
        pass.setSeatNo("12A");
        return pass;
    }
}
