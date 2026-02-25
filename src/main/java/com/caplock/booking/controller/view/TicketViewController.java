package com.caplock.booking.controller.view;

import com.caplock.booking.entity.dto.CreateTicketDTO;
import com.caplock.booking.entity.dto.TicketDto;
import com.caplock.booking.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/tickets")
@RequiredArgsConstructor
public class TicketViewController {
    private final TicketService ticketService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", ticketService.findAll());
        return "ui/tickets/list";
    }

    @GetMapping("/booking/{bookingId}")
    public String getTicketsByBookingId(@PathVariable Long bookingId, Model model) {
        model.addAttribute("tickets", ticketService.findByBookingId(bookingId));
        return "ui/tickets/confirmation";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new TicketDto());
        model.addAttribute("formAction", "/ui/tickets");
        return "ui/tickets/forms/form";
    }

    @PostMapping
    public String create(@ModelAttribute("item") CreateTicketDTO dto) {
        ticketService.create(dto);
        return "redirect:/ui/tickets";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        TicketDto dto = ticketService.getById(id).orElseThrow();
        model.addAttribute("item", dto);
        model.addAttribute("formAction", "/ui/tickets/" + id);
        return "ui/tickets/forms/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("item") TicketDto dto) {
        ticketService.update(id, dto);
        return "redirect:/ui/tickets";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        ticketService.deleteById(id);
        return "redirect:/ui/tickets";
    }
}
