package com.caplock.booking.Model.DAO;

import java.time.LocalDate;

public class PaymentDAO {
    private Long id;
    private int bookingId;
    private int status;
    private String paymentMethod;
    private LocalDate paidAt;

    public PaymentDAO(Long id, int bookingId, int status, String paymentMethod, LocalDate paidAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paidAt = paidAt;
    }
    public PaymentDAO() {

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
