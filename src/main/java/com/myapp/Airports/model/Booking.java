package com.myapp.Airports.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a flight booking in the system.
 * <p>
 * Each booking links a user to a specific flight and stores
 * booking metadata such as creation time and booking reference.
 * </p>
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @Column(name = "book_ref", nullable = false)
    private String bookRef;

    @Column(name = "book_date", nullable = false)
    private LocalDateTime bookDate;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();;

    public Booking () {}

    public Booking(String ref, LocalDateTime date, BigDecimal amount) {
        this.bookRef = ref;
        this.bookDate = date;
        this.totalAmount = amount;
    }

    public String getBookRef() {
        return bookRef;
    }

    public void setBookRef(String bookRef) {
        this.bookRef = bookRef;
    }

    public LocalDateTime getBookDate() {
        return bookDate;
    }

    public void setBookDate(LocalDateTime bookDate) {
        this.bookDate = bookDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
