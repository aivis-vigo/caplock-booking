package com.caplock.booking.Model.DAO;

import java.time.LocalDateTime;

public class InvoiceDAO {
    private Long id;
    private double amount;
    private int invoiceNumber;
    private LocalDateTime createdAt;

    public InvoiceDAO(Long id, double amount, int invoiceNumber, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.invoiceNumber = invoiceNumber;
        this.createdAt = createdAt;
    }

    public InvoiceDAO() {

    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public int getInvoiceNumber() {
        return invoiceNumber;
    }
    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
