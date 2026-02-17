package com.caplock.booking.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long bookingId;
    private String status;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDate paidAt;

}
