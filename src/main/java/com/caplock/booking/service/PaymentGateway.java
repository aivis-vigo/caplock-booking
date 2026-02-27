package com.caplock.booking.service;

import java.math.BigDecimal;
import java.util.Random;

public interface PaymentGateway {
    record TransactionResult(String transactionId, boolean success, String message) {
    }


    default TransactionResult processPayment(Long bookingId, BigDecimal amount, String paymentMethod) {
        Random random = new Random();
        int i = random.nextInt(2);
        return new TransactionResult("txn_" + bookingId + "_" + System.currentTimeMillis(), i % 2 == 0, "Payment processed successfully");
    }
}
