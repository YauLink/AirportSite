package com.myapp.Airports.controller.web;

import com.myapp.Airports.model.Flying;
import com.myapp.Airports.view.api.IAirportsView;
import com.myapp.Airports.view.api.IFlyingsView;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/filters")
public class FilterController {

    private final IAirportsView airportView;
    private final IFlyingsView flyingsView;

    public FilterController(IAirportsView airportView, IFlyingsView flyingsView) {
        this.airportView = airportView;
        this.flyingsView = flyingsView;
    }

    @GetMapping
    public String showFilterForm(
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(name = "airport_out", required = false) String airportOut,
            @RequestParam(name = "airport_in", required = false) String airportIn,
            Model model) {

        model.addAttribute("airports", airportView.getAll());

        String normalizedOut = (airportOut == null || airportOut.isBlank()) ? null : airportOut;
        String normalizedIn = (airportIn == null || airportIn.isBlank()) ? null : airportIn;

        if (normalizedOut != null && normalizedIn != null) {

            IFlyingsView.FlyingFilter filter = new IFlyingsView.FlyingFilter(
                    normalizedOut,
                    normalizedIn,
                    currentPage
            );

            List<Flying> flights = flyingsView.getList(filter);
            long totalItems = flyingsView.count(filter);
            long totalPages = (long) Math.ceil(totalItems / 20.0);

            model.addAttribute("flying", flights);
            model.addAttribute("maxCountFlying", totalPages);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("currentAirportOut", normalizedOut);
            model.addAttribute("currentAirportIn", normalizedIn);
        } else {
            model.addAttribute("flying", null);
            model.addAttribute("maxCountFlying", 0);
            model.addAttribute("currentPage", 1);
            model.addAttribute("currentAirportOut", null);
            model.addAttribute("currentAirportIn", null);
        }

        return "filters";
    }

    @PostMapping
    public String handleFilter(
            @RequestParam("airport_out") String airportOut,
            @RequestParam("airport_in") String airportIn) {

        return "redirect:/filters?airport_out=" + airportOut +
                "&airport_in=" + airportIn +
                "&page=1";
    }
}
