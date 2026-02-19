package com.caplock.booking.controller;

import com.caplock.booking.entity.dto.CreateTicketDTO;
import com.caplock.booking.entity.dto.TicketDTO;
import com.caplock.booking.entity.dto.Response;
import com.caplock.booking.entity.object.Ticket;
import com.caplock.booking.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/")
    public String findAll(Model model) {
        List<TicketDTO> tickets = ticketService.findAll().getData();

        model.addAttribute("tickets", tickets);

        return "users/tickets/list";
    }

    @ResponseBody
    @GetMapping("/{holderName}")
    public Response<?> findByHolderName(@PathVariable String holderName) {
        return ticketService.findByHolderName(holderName);
    }

    @GetMapping("/create-form")
    public String getCreateTicketForm() {
        return "tickets/create-form";
    }

    @ResponseBody
    @PostMapping("/create")
    public Response<TicketDTO> createTicket(@RequestBody CreateTicketDTO newTicket) {
        return ticketService.create(newTicket);
    }

    @GetMapping("/edit-form")
    public String getEditTicketForm() {
        return "tickets/edit-form";
    }

    @ResponseBody
    @PutMapping("/edit/{id}")
    public Response<?> editTicket(@PathVariable Long id, @RequestBody Ticket updatedTicket) {
        return ticketService.update(id, updatedTicket);
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public Response<?> deleteTicket(@PathVariable Long id) {
        return ticketService.deleteById(id);
    }

}
