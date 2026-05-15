package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.model.*;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MVC controller responsible for user authentication and cabinet management.
 * <p>
 * Provides endpoints for login, logout, and displaying
 * user ticket information inside the personal cabinet.
 * </p>
 */
@Controller
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;
    private final TicketService ticketService;

    public AuthController(AuthService authService, TicketService ticketService) {
        this.authService = authService;
        this.ticketService = ticketService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            AuthResponseDTO auth = authService.login(username, password);

            session.setAttribute("USER_ID", auth.getUserId());
            session.setAttribute("USER_NAME", auth.getFullName());

            return "redirect:/user/cabinet";

        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "user/login";
        }
    }

    @GetMapping("/cabinet")
    public String cabinet(HttpSession session, Model model) {
        Object userIdObj = session.getAttribute("USER_ID");

        if (userIdObj == null) {
            return "redirect:/user/login";
        }

        String passengerId = String.valueOf(userIdObj);

        List<Ticket> tickets = ticketService.findAllByUserId(passengerId);

        model.addAttribute("fullName", session.getAttribute("USER_NAME"));
        model.addAttribute("tickets", tickets);

        return "user/cabinet";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}
