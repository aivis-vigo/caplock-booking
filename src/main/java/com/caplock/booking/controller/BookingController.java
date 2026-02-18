package com.caplock.booking.controller;

import com.caplock.booking.controller.helper.FormShower;
import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    Random random = new Random();
    @Autowired
    private final IBookingService bookingService;

    public BookingController(IBookingService iBookingService) {
        this.bookingService = iBookingService;
    }

    @GetMapping("/")
    public String getAllBookings(Model model) {
        // get user id from jwt
        long userId = 1;
        model.addAttribute("bookings", bookingService.getAllUserBookings(userId));
        return "bookings/bookings";
    }

    @GetMapping({"/form", "/form/{id}"})
    public String form(Model model, @PathVariable(required = false) Long id) {
        long safeId = (id == null) ? -1 : id;
        return FormShower.showForm(model, safeId, bookingService::getBookingById, BookingDto.class);
    }

    @PostMapping("/submitForm")
    public String setBooking(@ModelAttribute("booking") BookingDto booking) {


        var a = new ArrayList<String>() {
        };
        a.add("1");
        a.add("1");
        a.add("3");
        booking.setSeats(a);
        var result = bookingService.setNewBooking(booking);

        boolean isSuccess = result.getValue0();
        String message = result.getValue1();

        if (!isSuccess && message.contains("Booking full")) {
            int userId = (int) -1;
            // show message
            return "redirect:/waitList/form/" + userId;
        } else if (!isSuccess) {
            // show message
            return "redirect:/bookings/form/";
        }

            return "redirect:/bookings/";

    }

}
