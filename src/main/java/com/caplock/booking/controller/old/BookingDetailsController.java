package com.caplock.booking.controller.old;

import com.caplock.booking.service.old.IBookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings/details")
public class BookingDetailsController {
    private final IBookingService bookingService;

    public BookingDetailsController(IBookingService iBookingService) {
        this.bookingService = iBookingService;
    }

    @GetMapping("/view/{id}")
    public String getDetails(@PathVariable String id, Model model) {
        var a=bookingService.getDetails(id);
        model.addAttribute("bookingDetails",a );
        return "bookings/bookingDetails";
    }


}
