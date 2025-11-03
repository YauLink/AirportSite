package com.myapp.Airports.controller.web;

import com.myapp.Airports.model.Airport;
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
    public String showFilterForm(@RequestParam(name = "page", defaultValue = "1", required = false) int currentPage,
                                 @RequestParam(name = "airport_out", required = false) String airportOut,
                                 @RequestParam(name = "airport_in", required = false) String airportIn,
                                 Model model) {
        List<Airport> airports = airportView.getAll();
        model.addAttribute("airports", airports);

        if ((airportOut != null && !airportOut.isBlank()) || (airportIn != null && !airportIn.isBlank())) {
            var filter = new IFlyingsView.FlyingFilter(
                    airportOut.isBlank() ? null : airportOut,
                    airportIn.isBlank() ? null : airportIn,
                    currentPage
            );

            List<Flying> flying = flyingsView.getList(filter);
            long maxCountFlying = flyingsView.count(filter);

            model.addAttribute("flying", flying);
            model.addAttribute("maxCountFlying", maxCountFlying);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("currentAirportOut", airportOut);
            model.addAttribute("currentAirportIn", airportIn);
        }

        return "filters";
    }

    @PostMapping
    public String handleFilter(@RequestParam("airport_out") String airportOut,
                               @RequestParam("airport_in") String airportIn,
                               @RequestParam(name = "page", defaultValue = "1", required = false) int currentPage,
                               Model model) {
        return showFilterForm(currentPage, airportOut, airportIn, model);
    }

    /*@GetMapping
    public ResponseEntity<Map<String, Object>> filterFlights(
            @RequestParam(name = "airport_out", required = false) String airportOut,
            @RequestParam(name = "airport_in", required = false) String airportIn,
            @RequestParam(name = "page", defaultValue = "1") int currentPage
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("airports", airportView.getAll());

        if ((airportOut != null && !airportOut.isBlank()) || (airportIn != null && !airportIn.isBlank())) {
            var filter = new IFlyingsView.FlyingFilter(
                    airportOut.isBlank() ? null : airportOut,
                    airportIn.isBlank() ? null : airportIn,
                    currentPage
            );

            List<Flying> flying = flyingsView.getList(filter);
            long total = flyingsView.count(filter);

            response.put("flights", flying);
            response.put("totalCount", total);
            response.put("currentPage", currentPage);
        }

        return ResponseEntity.ok(response);
    }*/
}
