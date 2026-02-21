package com.caplock.booking.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PaymentSucceededEvent {
    private final Long paymentId;
    private final Long bookingId;
    private final BigDecimal amount;
    private final String method;
    private final String transactionId;
    private final LocalDateTime paidAt;
}
