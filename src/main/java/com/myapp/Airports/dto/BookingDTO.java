package com.myapp.Airports.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingDTO {

    private String bookRef;
    private LocalDateTime bookDate;
    private BigDecimal totalAmount;

    public BookingDTO(String bookRef, LocalDateTime bookDate, BigDecimal totalAmount) {
        this.bookRef = bookRef;
        this.bookDate = bookDate;
        this.totalAmount = totalAmount;
    }

    public BookingDTO() {

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
}
