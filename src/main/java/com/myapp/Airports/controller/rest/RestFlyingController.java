package com.myapp.Airports.controller.rest;

import com.myapp.Airports.dto.FlyingDTO;
import com.myapp.Airports.mapper.FlyingMapper;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.service.FlyingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flights")
public class RestFlyingController {

    private final FlyingService flyingService;

    public RestFlyingController(FlyingService flyingService) {
        this.flyingService = flyingService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<FlyingDTO>> getAllFlights() {
        List<FlyingDTO> flights = flyingService.findAll()
                .stream()
                .map(FlyingMapper::toDto)
                .toList();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<FlyingDTO> getFlightById(@PathVariable Integer id) {
        Optional<Flying> flightOpt = flyingService.findById(id);
        return flightOpt
                .map(flight -> ResponseEntity.ok(FlyingMapper.toDto(flight)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<FlyingDTO> createFlightApi(@RequestBody @Valid FlyingDTO flyingDTO) {
        Flying saved = flyingService.saveAndReturn(FlyingMapper.toEntity(flyingDTO));
        return ResponseEntity.ok(FlyingMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<FlyingDTO> updateFlightApi(@PathVariable Integer id,
                                                     @RequestBody @Valid FlyingDTO flyingDTO) {
        Optional<Flying> existing = flyingService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Flying flight = FlyingMapper.toEntity(flyingDTO);
        flight.setId(id);
        Flying updated = flyingService.saveAndReturn(flight);
        return ResponseEntity.ok(FlyingMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteFlightApi(@PathVariable Integer id) {
        flyingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
