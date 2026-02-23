package com.caplock.booking.service;

import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.BookingItemDto;
import com.caplock.booking.entity.dto.EventDetailsDto;
import com.caplock.booking.entity.dto.PaymentDto;
import org.javatuples.Triplet;

import java.util.List;

public interface FlowService {
    Triplet<Boolean, String, Object> processBookingFlow(Long userId,
                                                        EventDetailsDto eventDetails,
                                                        BookingDto bookingDto,
                                                        List<BookingItemDto> bookingItemDto,
                                                        PaymentDto paymentDto);

    Triplet<Boolean, String, Object> processBookingCancellationFlow(Long userId,
                                                                    Long bookingId);
}
