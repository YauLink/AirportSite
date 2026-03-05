package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.model.Booking;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.BookingService;
import com.myapp.Airports.service.FlyingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * MVC controller responsible for admin cabinet.
 * <p>
 * Provides endpoints for creating and retrieving information about flights and logs.
 * </p>
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final FlyingService flyingService;
    private final BookingService bookingService;
    private final AuthService authService;

    public AdminController(FlyingService flyingService,
                           BookingService bookingService, AuthService authService) {
        this.flyingService = flyingService;
        this.bookingService = bookingService;
        this.authService = authService;
    }

    @GetMapping(produces = {"text/html"})
    protected String hello(Model model) {
        return "admin/index";
    }

    @GetMapping(value = "/login",produces = {"text/html"})
    protected String login(@RequestParam String username,
                           @RequestParam String password,
                           HttpSession session,
                           Model model) {
        try {
            //Call the Authentification Service which calls the User Management REST API
            AuthResponseDTO auth = authService.login(username, password);

            session.setAttribute("USER_ID", auth.getUserId());
            session.setAttribute("USER_NAME", auth.getFullName());

            return "redirect:/dashboard";

        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
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
        model.addAttribute("flights", flyingService.findAll());
        return "admin/flights";
    }

    @GetMapping(value = "/reports/bookings", produces = "text/html")
    public String bookingsReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        Page<Booking> bookings = bookingService.findAll(page, size);

        model.addAttribute("bookings", bookings.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookings.getTotalPages());

        return "admin/reports/bookings";
    }

    @GetMapping(value = "/reports/flights", produces = "text/html")
    public String flightsReport(Model model) {
        model.addAttribute("flights", flyingService.findAll());
        return "admin/reports/flights";
    }

    @GetMapping(value = "/reports/users", produces = "text/html")
    public String usersReport(Model model) {
        model.addAttribute("integrationPending", true);
        return "admin/reports/users";
    }

    @GetMapping(value = "/logs", produces = "text/html")
    public String systemLogs(Model model) {
        model.addAttribute("notImplemented", true);
        return "admin/logs";
    }

    @GetMapping(value = "/settings", produces = {"text/html"})
    protected String settings(Model model) {
        model.addAttribute("theme", "dark");
        model.addAttribute("notificationsEnabled", true);
        return "admin/settings";
    }
}
