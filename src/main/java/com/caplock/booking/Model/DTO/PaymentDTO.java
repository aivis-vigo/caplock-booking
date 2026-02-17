package com.caplock.booking.Model.DTO;

import java.time.LocalDate;

public class PaymentDTO {
    private Long id;
    private int bookingId;
    private int status;
    private String paymentMethod;
    private LocalDate paidAt;

    public PaymentDTO(Long id, int bookingId, int status, String paymentMethod, LocalDate paidAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paidAt = paidAt;
    }
    public PaymentDTO() {

    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getBookingId() {
        return bookingId;
    }
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public LocalDate getPaidAt() {
        return paidAt;
    }
    public void setPaidAt(LocalDate paidAt) {
        this.paidAt = paidAt;
    }
}
