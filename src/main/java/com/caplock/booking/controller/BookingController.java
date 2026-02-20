package com.caplock.booking.controller;

import com.caplock.booking.controller.helper.FormShower;
import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.BookingFormDto;
import com.caplock.booking.entity.dto.Seat;
import com.caplock.booking.service.IBookingService;
import com.caplock.booking.service.IEventService;
import org.javatuples.Pair;
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
    @Autowired
    private final IEventService eventService;

    public BookingController(IBookingService iBookingService, IEventService iEventService) {
        this.bookingService = iBookingService;
        this.eventService = iEventService;
    }

    @GetMapping("/form")
    public String formRedirect(@RequestParam(required = false) String id) {
        if (id == null || id.isBlank()) {
            return "redirect:/bookings/form/"; // or open add form
        }
        return "redirect:/bookings/form/" + id + "/";
    }

    @GetMapping("/")
    public String getAllBookings(Model model) {
        // get user id from jwt
        long userId = 1;
        model.addAttribute("testId", 0L);
        model.addAttribute("bookingList", bookingService.getAllUserBookings(userId));
        return "bookings/bookings";
    }

    @GetMapping({"/form/{eventId}/", "/form/{eventId}/{id}"})
    public String form(Model model, @PathVariable long eventId, @PathVariable(required = false) String id) {
        long userId = 1;
        boolean editing = id != null;
        var val = bookingService.getBookingFormById(id, userId);
        var form = val == null ? new BookingFormDto() : val;

        form.setEventId(eventId);
        form.setUserId(userId);

        var seats = eventService.getSeatsFreeForEvent(eventId);
        model.addAttribute("editing", editing);
        model.addAttribute("availableSeats", seats);
        model.addAttribute("bookingForm", form);
        model.addAttribute("formName", editing ? "Edit" : "Add");
        model.addAttribute("formButton", editing ? "Update" : "Place " + "bookingForm");
        return "bookings/bookingForm";
    }

    @PostMapping("/submitForm")
    public String setBooking(@ModelAttribute("bookingForm") BookingFormDto booking) {
        booking.setSeats(List.of(new Seat("A", "1", "2")));


        var result = bookingService.setNewBooking(booking);

        boolean isSuccess = result.getValue0();
        String message = result.getValue1();

        if (!isSuccess && message.contains("Booking full")) {
            int userId = (int) -1;
            // show message
            return "redirect:/waitList/form/" + userId;
        } else if (!isSuccess) {
            // show message
            return "redirect:/bookings/form/" + booking.getEventId() + "/";
        }

        return "redirect:/bookings/";

    }

    @PutMapping("/submitEditForm")
    public String updateBooking(@ModelAttribute("bookingForm") BookingFormDto booking) {

        booking.setSeats(List.of(new Seat("A", "1", "2")));


        boolean cancelSuccess = bookingService.cancelBooking(booking.getBookingId());

        if (!cancelSuccess) {
            return "redirect:/bookings/form/" + booking.getEventId() + "/";
        }
        var result = bookingService.setNewBooking(booking);
        boolean isSuccess = result.getValue0();
        String message = result.getValue1();

        if (!isSuccess && message.contains("Booking full")) {
            int userId = (int) -1;
            // show message
            return "redirect:/waitList/form/" + userId;
        } else if (!isSuccess) {
            // show message
            return "redirect:/bookings/form/" + booking.getEventId() + "/";
        }

        return "redirect:/bookings/";

    }

    @PostMapping("/delete/{id}")
    public String deleteBooking(@PathVariable String id) {
        if (bookingService.cancelBooking(id))
            return "redirect:/bookings/";
        else {
            //show error
            return "redirect:/bookings/";
        }
    }
}
