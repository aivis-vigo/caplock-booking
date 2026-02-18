package com.caplock.booking.controller;

import com.caplock.booking.dto.Response;
import com.caplock.booking.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketSerivce;

    @GetMapping("/")
    public Response<?> findByHolderName(Model model) {
        return ticketSerivce.findByHolderName("John");
    }


}
