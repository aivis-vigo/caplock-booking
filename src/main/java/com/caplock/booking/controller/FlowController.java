package com.caplock.booking.controller;

import com.caplock.booking.entity.dto.*;
import com.caplock.booking.exception.SeatNotAssignedException;
import com.caplock.booking.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("api/v1/handle-booking")
@RequiredArgsConstructor
public class FlowController {

    private final FlowService flowService;
    private final TicketService ticketService;

    @PostMapping
    public String handleBooking(@ModelAttribute BookingRequestDTO request, Model model, RedirectAttributes redirectAttributes) {
        log.info("Started processing booking with id: {}", request.getEventId());
        try {

            Long bookingId = flowService.handleBooking(request);
            if(bookingId==null){
                log.error("Booking failed for event {}", request.getEventId());
                redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong during booking. Please try again later.");
                return "redirect:/ui/bookings";
            }

            model.addAttribute("tickets", ticketService.findByBookingId(bookingId));
            return "/ui/tickets/confirmation";
        } catch (SeatNotAssignedException e) {
            log.error("Seat assignment failed for event {}: {}", request.getEventId(), e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/ui/bookings";

        }
    }

}
