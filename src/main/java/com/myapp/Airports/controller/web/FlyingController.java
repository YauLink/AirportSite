package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.FlyingDTO;
import com.myapp.Airports.mapper.FlyingMapper;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.service.FlyingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/flights")
public class FlyingController {

    private final FlyingService flyingService;

    public FlyingController(FlyingService flyingService) {
        this.flyingService = flyingService;
    }

    @GetMapping
    public String listFlights(Model model) {
        model.addAttribute("flights", flyingService.findAll());
        return "flights_list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flyingDTO", new FlyingDTO());
        return "flight_form";
    }

    @PostMapping("/new")
    public String createFlight(@Valid @ModelAttribute("flyingDTO") FlyingDTO flyingDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "flight_form";
        }

        Flying flying = FlyingMapper.toEntity(flyingDTO);
        flyingService.save(flying);
        return "redirect:/flights";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Optional<Flying> flightOpt = flyingService.findById(id);
        if (flightOpt.isPresent()) {
            FlyingDTO dto = FlyingMapper.toDto(flightOpt.get());
            model.addAttribute("flyingDTO", dto);
            return "flight_form";
        } else {
            return "redirect:/flights";
        }
    }

    @PostMapping("/edit")
    public String updateFlight(@Valid @ModelAttribute("flyingDTO") FlyingDTO flyingDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "flight_form";
        }

        Flying flying = FlyingMapper.toEntity(flyingDTO);
        flyingService.save(flying); // will perform update if ID exists
        return "redirect:/flights";
    }

    @PostMapping("/delete/{id}")
    public String deleteFlight(@PathVariable("id") Integer id) {
        flyingService.deleteById(id);
        return "redirect:/flights";
    }

    /* ---------------- REST API ENDPOINTS ---------------- */

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<FlyingDTO>> getAllFlights() {
        List<FlyingDTO> flights = flyingService.findAll()
                .stream()
                .map(FlyingMapper::toDto)
                .toList();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<FlyingDTO> getFlightById(@PathVariable Integer id) {
        Optional<Flying> flightOpt = flyingService.findById(id);
        return flightOpt
                .map(flight -> ResponseEntity.ok(FlyingMapper.toDto(flight)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<FlyingDTO> createFlightApi(@RequestBody @Valid FlyingDTO flyingDTO) {
        Flying saved = flyingService.save(FlyingMapper.toEntity(flyingDTO));
        return ResponseEntity.ok(FlyingMapper.toDto(saved));
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<FlyingDTO> updateFlightApi(@PathVariable Integer id,
                                                     @RequestBody @Valid FlyingDTO flyingDTO) {
        Optional<Flying> existing = flyingService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Flying flight = FlyingMapper.toEntity(flyingDTO);
        //flight.setId(id);
        //Flying updated = flyingService.save(flight);
        return ResponseEntity.ok(FlyingMapper.toDto(updated));
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteFlightApi(@PathVariable Integer id) {
        flyingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
