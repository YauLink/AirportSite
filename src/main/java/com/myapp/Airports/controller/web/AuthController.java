package com.myapp.Airports.controller.web;

import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.exceptions.UserNotFoundException;
import com.myapp.Airports.model.*;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.expiration:3600000}")
    private long jwtExpiration;

    @Value("${security.jwt.cookie-secure:true}")
    private boolean secureCookie;

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

            response.addHeader(
                    HttpHeaders.SET_COOKIE,
                    jwtCookie(auth.getToken(), Duration.ofMillis(jwtExpiration))
                            .toString()
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

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal()
                instanceof JwtUserPrincipal user)) {

            return "redirect:/user/login";
        }

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

    @PostMapping("/logout")
    public String logout(
            HttpServletResponse response) {

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                jwtCookie("", Duration.ZERO).toString()
        );

        return "redirect:/user/login";
    }

    private ResponseCookie jwtCookie(String token, Duration maxAge) {
        return ResponseCookie
                .from(JWT_COOKIE, token)
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")
                .build();
    }
}
