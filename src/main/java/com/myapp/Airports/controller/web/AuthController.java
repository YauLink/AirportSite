package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.exceptions.UserNotFoundException;
import com.myapp.Airports.model.*;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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

    private static final String JWT_COOKIE = "AIRPORTS_JWT";

    private final AuthService authService;
    private final TicketService ticketService;

    public AuthController(
            AuthService authService,
            TicketService ticketService) {

        this.authService = authService;
        this.ticketService = ticketService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response,
            Model model) {

        try {

            AuthResponseDTO auth =
                    authService.login(
                            username,
                            password
                    );

            ResponseCookie cookie =
                    ResponseCookie
                            .from(JWT_COOKIE, auth.getToken())
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(Duration.ofHours(1))
                            .sameSite("Lax")
                            .build();

            response.addHeader(
                    HttpHeaders.SET_COOKIE,
                    cookie.toString()
            );

            return "redirect:/user/cabinet";

        } catch (UserNotFoundException ex) {

            model.addAttribute(
                    "error",
                    ex.getMessage()
            );

            return "user/login";
        }
    }

    @GetMapping("/cabinet")
    public String cabinet(
            Authentication authentication,
            Model model) {

        if (!(authentication
                .getPrincipal()
                instanceof JwtUserPrincipal)) {

            return "redirect:/user/login";
        }

        JwtUserPrincipal user =
                (JwtUserPrincipal)
                        authentication.getPrincipal();

        String passengerId =
                String.valueOf(user.getUserId());

        List<Ticket> tickets =
                ticketService.findAllByUserId(passengerId);

        model.addAttribute(
                "fullName",
                user.getFullName()
        );

        model.addAttribute(
                "tickets",
                tickets
        );

        return "user/cabinet";
    }

    @GetMapping("/logout")
    public String logout(
            HttpServletResponse response) {

        ResponseCookie cookie =
                ResponseCookie
                        .from(JWT_COOKIE, "")
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(Duration.ZERO)
                        .sameSite("Lax")
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return "redirect:/user/login";
    }
}
