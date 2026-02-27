package com.caplock.booking.entity.dto;

import java.math.BigDecimal;

public record InvoiceFormDto(long bookingId, BigDecimal totalAmount, String discountCode, long paymentId,
                             String holderName, String holderEmail) {
}