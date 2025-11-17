package com.myapp.Airports.controller.rest;

import com.myapp.Airports.model.Flying;
import com.myapp.Airports.view.api.IAirportsView;
import com.myapp.Airports.view.api.IFlyingsView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/filters")
public class RestFilterController {


    private final IAirportsView airportView;
    private final IFlyingsView flyingsView;

    public RestFilterController(IAirportsView airportView, IFlyingsView flyingsView) {
        this.airportView = airportView;
        this.flyingsView = flyingsView;
    }

    @GetMapping
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
    }
}
