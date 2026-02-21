package com.caplock.booking.entity.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "holder_name")
    private String holderName;

    @Column(name = "holder_email")
    private String holderEmail;

    private BigDecimal subtotal;
    private BigDecimal discount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "pdf_url")
    private String pdfUrl;
}
