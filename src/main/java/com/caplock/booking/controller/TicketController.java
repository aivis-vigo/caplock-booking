package com.caplock.booking.controller;

import com.caplock.booking.dto.Response;
import com.caplock.booking.dto.TicketDTO;
import com.caplock.booking.entity.Ticket;
import com.caplock.booking.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/")
    public Response<?> findAll() {
        return ticketService.findAll();
    }

    @GetMapping("/{holderName}")
    public Response<?> findByHolderName(@PathVariable String holderName, Model model) {
        return ticketService.findByHolderName(holderName);
    }

    @GetMapping("/create-form")
    public String getCreateTicketForm() {
        return "tickets/create-form";
    }

    @PostMapping("/create")
    public Response<TicketDTO> createTicket(@RequestBody Ticket newTicket) {
        return ticketService.create(newTicket);
    }

}
