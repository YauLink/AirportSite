package com.myapp.Airports.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * REST controller responsible for admin cabinet.
 * <p>
 * Provides endpoints for creating and retrieving information about flights and logs.
 * </p>
 */
public class RestAdminController {

    @GetMapping("/api/dashboard")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> data = Map.of(
                "totalUsers", 3,
                "totalFlights", 3,
                "systemStatus", "OK"
        );
        return ResponseEntity.ok(data);
    }

    @GetMapping("/api/users")
    @ResponseBody
    public ResponseEntity<List<String>> getUsers() {
        return ResponseEntity.ok(List.of("Alice", "Bob", "Charlie"));
    }

    @GetMapping(value = "/api/flights", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<String>> getFlights() {
        return ResponseEntity.ok(List.of("Flight-101", "Flight-202", "Flight-303"));
    }

    @GetMapping("/api/settings")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSettings() {
        Map<String, Object> settings = Map.of(
                "theme", "dark",
                "notificationsEnabled", true
        );
        return ResponseEntity.ok(settings);
    }
}
