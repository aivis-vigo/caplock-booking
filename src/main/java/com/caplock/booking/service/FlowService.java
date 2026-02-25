package com.caplock.booking.service;

import com.caplock.booking.entity.dto.*;

public interface FlowService {
    Long handleBooking(BookingRequestDTO requestDTO);
}
