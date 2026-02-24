package com.caplock.booking.controller;

import com.caplock.booking.entity.StatusBookingEnum;
import com.caplock.booking.entity.StatusPaymentEnum;
import com.caplock.booking.entity.TicketType;
import com.caplock.booking.entity.dao.BookingEntity;
import com.caplock.booking.entity.dto.*;
import com.caplock.booking.service.*;
import com.caplock.booking.service.impl.SeatReservationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.TableHeaderUI;
import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("api/v1/handle-booking")
@RequiredArgsConstructor
public class FlowController {

    private final FlowService flowService;

    @PostMapping
    public String handleBooking(@ModelAttribute BookingRequestDTO request) throws InterruptedException {
        log.info("Started processing booking with id: {}", request.getEventId());

        flowService.handleBooking(request);

        return "redirect:/ui/bookings";
    }

}
