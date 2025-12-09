package com.myapp.Airports.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableCaching
public class RedisTestConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "bookings", "booking", // BookingService
                "tickets", "ticket",    // TicketService
                "flights", "flight"     // FlyingService
        );
    }
}