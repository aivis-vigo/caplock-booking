package com.caplock.booking.entity.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDAO {
    private Long id;
    private Long bookingId;
    private BigDecimal amount;
    private String invoiceNumber;
    private LocalDateTime createdAt;
    private String filePath;
}
