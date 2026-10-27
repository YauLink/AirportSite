package com.myapp.Airports.controller.rest;

import com.myapp.Airports.dto.AuthRequestDTO;
import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.dto.CabinetTicketDTO;
import com.myapp.Airports.exceptions.UserNotAuthenticatedException;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.model.JwtUserPrincipal;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.beans.factory.annotation.Value;
import java.time.Duration;
import com.myapp.Airports.dto.CabinetResponseDTO;

import java.util.List;

/**
 * REST controller responsible for user authentication and account data.
 * <p>
 * Provides endpoints for login, logout, and retrieving the authenticated
 * user's ticket cabinet.
 * </p>
 */
@RestController
@RequestMapping("/api/user")
public class RestAuthController {

    private final AuthService authService;
    private final TicketService ticketService;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpiration;

    @Value("${security.jwt.cookie-secure:true}")
    private boolean secureCookie;

    public RestAuthController(
            AuthService authService,
            TicketService ticketService) {
        this.authService = authService;
        this.ticketService = ticketService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody AuthRequestDTO request,
            HttpServletResponse response) {

        AuthResponseDTO auth = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        if (auth.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(auth);
        }

        if (auth.getToken() != null && !auth.getToken().isBlank()) {
            response.addHeader(HttpHeaders.SET_COOKIE,
                    ResponseCookie.from("AIRPORTS_JWT", auth.getToken())
                            .httpOnly(true)
                            .secure(secureCookie)
                            .sameSite("Lax")
                            .path("/")
                            .maxAge(Duration.ofMillis(jwtExpiration))
                            .build()
                            .toString());
        }

        return ResponseEntity.ok(auth);
    }

    @GetMapping("/cabinet")
    public ResponseEntity<CabinetResponseDTO> cabinet(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof JwtUserPrincipal user)) {
            throw new UserNotAuthenticatedException(
                    "User is not authenticated"
            );
        }

        String passengerId = String.valueOf(user.getUserId());

        List<Ticket> tickets = ticketService.findAllByUserId(passengerId);
        List<CabinetTicketDTO> cabinetTickets = tickets.stream()
                .map(ticket -> new CabinetTicketDTO(
                        ticket.getTicketNo(),
                        ticket.getBooking() == null ? null : ticket.getBooking().getBookRef(),
                        ticket.getPassengerName(),
                        ticket.getContactData()))
                .toList();

        return ResponseEntity.ok(
                new CabinetResponseDTO(user.getFullName(), cabinetTickets)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpSession session,
            HttpServletResponse response) {

        session.invalidate();
        response.addHeader(HttpHeaders.SET_COOKIE,
                ResponseCookie.from("AIRPORTS_JWT", "")
                        .httpOnly(true)
                        .secure(secureCookie)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(Duration.ZERO)
                        .build()
                        .toString());

        return ResponseEntity.ok("Logged out");
    }
}
