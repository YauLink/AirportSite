package com.myapp.Airports.service;

import com.myapp.Airports.AirportsApplication;
import com.myapp.Airports.config.RedisTestConfig;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.storage.api.IFlyingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AirportsApplication.class)
@Import(RedisTestConfig.class)
class FlyingServiceCacheTest {

    @Autowired
    private FlyingService flyingService;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private IFlyingsRepository flyingRepository;

    private Flying flight1;

    @BeforeEach
    void setUp() {
        flight1 = new Flying();
        flight1.setId(1);
        flight1.setFlightNo("FL123");
        cacheManager.getCache("flights").clear();
        cacheManager.getCache("flight").clear();
    }

    @Test
    void findAll_ShouldUseCache() {
        when(flyingRepository.findAll()).thenReturn(List.of(flight1));

        List<Flying> first = flyingService.findAll();
        assertNotNull(first);
        assertEquals(1, first.size());

        List<Flying> cached = flyingService.findAll();
        verify(flyingRepository, times(1)).findAll();
        assertNotNull(cached);
    }

    @Test
    void findById_ShouldUseCache() {
        when(flyingRepository.findById(1)).thenReturn(Optional.of(flight1));

        Optional<Flying> first = flyingService.findById(1);
        assertTrue(first.isPresent());
        assertEquals(flight1.getFlightNo(), first.get().getFlightNo());

        Optional<Flying> cached = flyingService.findById(1);
        verify(flyingRepository, times(1)).findById(1);
        assertTrue(cached.isPresent());
    }

    @Test
    void save_ShouldEvictCache() {
        when(flyingRepository.save(flight1)).thenReturn(flight1);
        when(flyingRepository.findAll()).thenReturn(List.of(flight1));

        flyingService.findAll();
        assertNotNull(cacheManager.getCache("flights").get(SimpleKey.EMPTY));

        flyingService.save(flight1);
        assertNull(cacheManager.getCache("flights").get(SimpleKey.EMPTY));
    }

    @Test
    void saveAndReturn_ShouldEvictCache() {
        when(flyingRepository.save(flight1)).thenReturn(flight1);
        when(flyingRepository.findAll()).thenReturn(List.of(flight1));

        flyingService.findAll();
        assertNotNull(cacheManager.getCache("flights").get(SimpleKey.EMPTY));

        flyingService.saveAndReturn(flight1);
        assertNull(cacheManager.getCache("flights").get(SimpleKey.EMPTY));
    }

    @Test
    void deleteById_ShouldEvictCache() {
        doNothing().when(flyingRepository).deleteById(1);

        flyingService.findAll();
        assertNotNull(cacheManager.getCache("flights").get(SimpleKey.EMPTY));

        flyingService.deleteById(1);
        assertNull(cacheManager.getCache("flights").get(SimpleKey.EMPTY));
        assertNull(cacheManager.getCache("flight").get(1));
    }
}
