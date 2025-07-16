package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.FlyingDTO;
import com.myapp.Airports.dto.FlyingMapper;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.service.FlyingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/flights")
public class FlyingController {

    private final FlyingService flyingService;

    public FlyingController(FlyingService flyingService) {
        this.flyingService = flyingService;
    }

    // Show list of all flights
    @GetMapping
    public String listFlights(Model model) {
        model.addAttribute("flights", flyingService.findAll());
        return "flight_list";
    }

    // Show create form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flyingDTO", new FlyingDTO());
        return "flight_form";
    }

    // Handle create flight
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

    // Show edit form
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

    // Handle edit submission
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

    // Delete a flight
    @PostMapping("/delete/{id}")
    public String deleteFlight(@PathVariable("id") Integer id) {
        flyingService.deleteById(id);
        return "redirect:/flights";
    }
}
