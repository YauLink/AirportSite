package com.myapp.Airports.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


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


    @GetMapping(value = "/users", produces = {"text/html"})
    protected String manageUsers(Model model) {
        model.addAttribute("users", List.of("Alice", "Bob", "Charlie"));
        return "admin/users";
    }

    @GetMapping(value = "/flights", produces = {"text/html"})
    protected String manageFlights(Model model) {
        model.addAttribute("flights", List.of("Flight-101", "Flight-202", "Flight-303"));
        return "admin/flights";
    }

    @GetMapping(value = "/settings", produces = {"text/html"})
    protected String settings(Model model) {
        model.addAttribute("theme", "dark");
        model.addAttribute("notificationsEnabled", true);
        return "admin/settings";
    }
}
