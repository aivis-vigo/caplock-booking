package com.caplock.booking.entity.dto;

import com.caplock.booking.entity.StatusPaymentEnum;
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
public class PaymentDto {
    private Long id;
    private Long bookingId;
    private BigDecimal amount;
    private StatusPaymentEnum status;
    private String method;
    private String transactionId;
    private String providerResponse;
}
