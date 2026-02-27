package com.caplock.booking.event;

public record paymentSucceededEvent(Long paymentId, Long bookingId, Boolean success) {}
