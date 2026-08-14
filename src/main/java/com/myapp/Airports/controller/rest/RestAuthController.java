package com.myapp.Airports.controller.rest;

import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.exceptions.UserNotAuthenticatedException;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
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
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        AuthResponseDTO auth = authService.login(username, password);

        session.setAttribute("USER_ID", auth.getUserId());
        session.setAttribute("USER_NAME", auth.getFullName());

        return ResponseEntity.ok(auth);
    }

    @GetMapping("/cabinet")
    public ResponseEntity<CabinetResponseDTO> cabinet(HttpSession session) {

        Object userIdObj = session.getAttribute("USER_ID");
        if (userIdObj == null) {
            throw new UserNotAuthenticatedException("User is not authenticated");
        }

        String passengerId = String.valueOf(userIdObj);

        List<Ticket> tickets = ticketService.findAllByUserId(passengerId);

        String userName = (String) session.getAttribute("USER_NAME");

        return ResponseEntity.ok(new CabinetResponseDTO(userName, tickets)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpSession session) {

        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }
}