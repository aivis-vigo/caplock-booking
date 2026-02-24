package com.caplock.booking.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentSucceededEvent(Long paymentId, Long bookingId, Boolean Success) {}
