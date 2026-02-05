package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.webauthn.api.AuthenticatorResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            // Call the AuthService which calls the User Management REST API
            AuthResponseDTO auth = authService.login(username, password);

            session.setAttribute("USER_ID", auth.getUserId());
            session.setAttribute("USER_NAME", auth.getFullName());

            //return "redirect:/cabinet";

        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}
