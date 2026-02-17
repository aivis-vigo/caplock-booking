package com.caplock.booking.controller;

import com.caplock.booking.service.IBookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bookings/details")
public class BookingDetailsController {
    private final IBookingService bookingService;

    public BookingDetailsController(IBookingService iBookingService) {
        this.bookingService = iBookingService;
    }

    @GetMapping("/view/{id}")
    public String getDetails(@PathVariable long id, Model model) {
        model.addAttribute("bookingDetails", List.of(bookingService.getDetails(id)));
        return "bookings/bookingDetails";
    }


    @PostMapping("/delete/{id}")
    public String deleteBooking(@PathVariable long id) {
        if (bookingService.cancelBooking(id))
            return "redirect:/bookings/bookings";
        else {
            //show error
            return "redirect:/bookings/bookingDetails";
        }
    }
}
