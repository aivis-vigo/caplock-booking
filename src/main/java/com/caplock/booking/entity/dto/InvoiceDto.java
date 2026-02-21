package com.caplock.booking.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvoiceDto {
    private Long id;
    private Long bookingId;
    private Long paymentId;
    private String invoiceNumber;
    private String holderName;
    private String holderEmail;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private LocalDateTime issuedAt;
    private String pdfUrl;
}
