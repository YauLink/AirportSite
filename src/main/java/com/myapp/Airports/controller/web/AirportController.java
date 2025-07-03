package com.myapp.Airports.controller.web;

import com.myapp.Airports.view.api.IAirportsView;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.myapp.Airports.view.*;
import java.util.List;

@Controller
@RequestMapping("/airports")
public class AirportController {

    private IAirportsView airportView;

    public AirportController(IAirportsView airportView) {
        this.airportView = airportView;
    }

    @GetMapping(produces = {"text/html"})
    protected String getAllHtml(Model model) {
        model.addAttribute("airports", this.airportView.getAll());
        return "airports";
    }

    /*@GetMapping("/{id}")
    protected String getOne(Model model, @PathVariable Long id) {
        model.addAttribute("airports", this.airportView.getAll());
        return "airport";
    }*/

}
