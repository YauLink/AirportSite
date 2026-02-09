package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            //Call the Authentification Service which calls the User Management REST API
            AuthResponseDTO auth = authService.login(username, password);

            session.setAttribute("USER_ID", auth.getUserId());
            session.setAttribute("USER_NAME", auth.getFullName());

            return "redirect:/cabinet";

        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/cabinet")
    public String cabinet(HttpSession session, Model model) {
        String passengerId = (String) session.getAttribute("USER_ID");

        if (passengerId == null) {
            return "redirect:/login";
        }

        List<Ticket> tickets = ticketService.findAllByUserId(passengerId);

        model.addAttribute("fullName", session.getAttribute("USER_NAME"));
        model.addAttribute("tickets", tickets);

        return "cabinet";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
