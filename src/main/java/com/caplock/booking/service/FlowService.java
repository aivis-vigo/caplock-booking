package com.caplock.booking.service;

import com.caplock.booking.entity.dto.*;
import org.javatuples.Triplet;

import java.util.List;

public interface FlowService {
    void handleBooking(BookingRequestDTO requestDTO);
}
