package com.caplock.booking.controller;

import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.service.IBookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/booking")
public class BookingController {
    private final IBookingService BookingService;

    public BookingController(IBookingService iBookingService) {
        this.BookingService = iBookingService;
    }


    @GetMapping("/submit-form")
    public String setBooking(@ModelAttribute BookingDto booking) {
        return "redirect:bookigs/booking-form";
    }
}
