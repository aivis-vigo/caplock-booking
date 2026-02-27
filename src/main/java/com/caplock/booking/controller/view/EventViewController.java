package com.caplock.booking.controller.view;

import com.caplock.booking.entity.PaymentMethodEnum;
import com.caplock.booking.entity.dto.EventDetailsDto;
import com.caplock.booking.entity.dto.EventDto;
import com.caplock.booking.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/events")
@RequiredArgsConstructor
public class EventViewController {
    private final EventService eventService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", eventService.getAll());
        return "ui/events/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new EventDto());
        model.addAttribute("formAction", "/ui/events");
        return "ui/events/form";
    }

    @PostMapping
    public String create(@ModelAttribute("item") EventDto dto) {
        eventService.create(dto);
        return "redirect:/ui/events";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        EventDto dto = eventService.getById(id).orElseThrow();
        model.addAttribute("item", dto);
        model.addAttribute("formAction", "/ui/events/" + id);
        return "ui/events/form";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable Long id, @ModelAttribute("event") EventDetailsDto dto, Model model) {
        EventDetailsDto response = eventService.getEventDetailsByEventId(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        model.addAttribute("event", response.getEvent());
        model.addAttribute("ticketConfig", response.getTicketConfig());
        model.addAttribute("freeSeats", response.getFreeSeats());
        model.addAttribute("paymentMethodOptions", PaymentMethodEnum.values());

        return "/ui/events/details";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("item") EventDto dto) {
        eventService.update(id, dto);
        return "redirect:/ui/events";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        eventService.delete(id);
        return "redirect:/ui/events";
    }
}
