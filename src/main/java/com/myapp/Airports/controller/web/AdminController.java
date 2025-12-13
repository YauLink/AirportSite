package com.myapp.Airports.controller.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @GetMapping(produces = {"text/html"})
    protected String hello(Model model) {
        return "admin/index";
    }


    @GetMapping(value = "/dashboard",produces = {"text/html"})
    protected String dashboard(Model model) {
        return "admin/dashboard";
    }

    @GetMapping(value = "/users", produces = "text/html")
    public String manageUsers(Model model) {
        return "admin/users";
    }

    @GetMapping(value = "/flights", produces = "text/html")
    public String manageFlights(Model model) {
        return "admin/flights";
    }

    @GetMapping(value = "/reports/bookings", produces = "text/html")
    public String bookingsReport(Model model) {
        return "admin/reports/bookings";
    }

    @GetMapping(value = "/reports/flights", produces = "text/html")
    public String flightsReport(Model model) {
        return "admin/reports/flights";
    }

    @GetMapping(value = "/reports/users", produces = "text/html")
    public String usersReport(Model model) {
        return "admin/reports/users";
    }

    @GetMapping(value = "/logs", produces = "text/html")
    public String systemLogs(Model model) {
        return "admin/logs";
    }

    @GetMapping(value = "/settings", produces = {"text/html"})
    protected String settings(Model model) {
        model.addAttribute("theme", "dark");
        model.addAttribute("notificationsEnabled", true);
        return "admin/settings";
    }
}
