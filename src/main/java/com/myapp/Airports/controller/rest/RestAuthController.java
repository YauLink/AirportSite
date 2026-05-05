package com.myapp.Airports.controller.rest;

import com.myapp.Airports.dto.AuthResponseDTO;
import com.myapp.Airports.model.Ticket;
import com.myapp.Airports.service.AuthService;
import com.myapp.Airports.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class RestAuthController {

    private final AuthService authService;
    private final TicketService ticketService;

    public RestAuthController(AuthService authService, TicketService ticketService) {
        this.authService = authService;
        this.ticketService = ticketService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password,
                                   HttpSession session) {
        try {
            AuthResponseDTO auth = authService.login(username, password);

            session.setAttribute("USER_ID", auth.getUserId());
            session.setAttribute("USER_NAME", auth.getFullName());

            return ResponseEntity.ok(auth);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid username or password");
        }
    }

    @GetMapping("/cabinet")
    public ResponseEntity<?> cabinet(HttpSession session) {
        Object userIdObj = session.getAttribute("USER_ID");

        if (userIdObj == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String passengerId = String.valueOf(userIdObj);

        List<Ticket> tickets = ticketService.findAllByUserId(passengerId);

        return ResponseEntity.ok()
                .body(new CabinetResponse(
                        (String) session.getAttribute("USER_NAME"),
                        tickets
                ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }

    // DTO for response
    public static class CabinetResponse {
        private String fullName;
        private List<Ticket> tickets;

        public CabinetResponse(String fullName, List<Ticket> tickets) {
            this.fullName = fullName;
            this.tickets = tickets;
        }

        public String getFullName() {
            return fullName;
        }

        public List<Ticket> getTickets() {
            return tickets;
        }
    }
}