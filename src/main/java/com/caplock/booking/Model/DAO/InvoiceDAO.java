package com.caplock.booking.Model.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDAO {
    private Long id;
    private double amount;
    private int invoiceNumber;
    private LocalDateTime createdAt;
}
