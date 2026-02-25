package com.caplock.booking.entity;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {
    STRIPE("Stripe"),
    PAYPAL("PayPal"),
    BANK("Bank Transfer");

    private final String label;

    PaymentMethodEnum(String label) {
        this.label = label;
    }
}
