package com.myapp.Airports.controller;

import com.myapp.Airports.dto.FlyingDTO;
import com.myapp.Airports.mapper.FlyingMapper;
import com.myapp.Airports.model.Flying;
import com.myapp.Airports.service.FlyingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flights")
public class FlyingController {

    private final FlyingService flyingService;

    public FlyingController(FlyingService flyingService) {
        this.flyingService = flyingService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flyingDTO", new FlyingDTO());
        return "flight_form";  // your Thymeleaf view
    }

    @PostMapping("/new")
    public String createFlight(@Valid @ModelAttribute("flyingDTO") FlyingDTO flyingDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "flight_form";  // re-render form with error messages
        }

        Flying flying = FlyingMapper.toEntity(flyingDTO);
        flyingService.save(flying);
        return "redirect:/flights";
    }
}
