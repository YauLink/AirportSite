package com.myapp.Airports.controller.rest;

import com.myapp.Airports.dto.AuthRequestDTO;
import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.exceptions.UserNotAuthenticatedException;
import com.myapp.Airports.model.JwtUserPrincipal;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.myapp.Airports.dto.CabinetResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class RestAuthController {

    private final AuthService authService;
    private final TicketService ticketService;

    public RestAuthController(
            AuthService authService,
            TicketService ticketService) {

        this.authService = authService;
        this.ticketService = ticketService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody AuthRequestDTO request) {

        AuthResponseDTO auth = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        if (auth.getUserId() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(auth);
        }

        return ResponseEntity.ok(auth);
    }

    @GetMapping("/cabinet")
    public ResponseEntity<CabinetResponseDTO> cabinet(
            Authentication authentication) {

        if (authentication == null
                || !(authentication.getPrincipal()
                instanceof JwtUserPrincipal)) {

            throw new UserNotAuthenticatedException(
                    "User is not authenticated"
            );
        }

        JwtUserPrincipal user =
                (JwtUserPrincipal)
                        authentication.getPrincipal();

        String passengerId =
                String.valueOf(user.getUserId());

        List<Ticket> tickets =
                ticketService.findAllByUserId(passengerId);

        return ResponseEntity.ok(
                new CabinetResponseDTO(
                        user.getFullName(),
                        tickets
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        return ResponseEntity.ok("Logged out");
    }
}