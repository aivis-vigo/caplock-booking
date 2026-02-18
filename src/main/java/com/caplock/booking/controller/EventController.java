package com.caplock.booking.controller;

import com.caplock.booking.controller.helper.FormShower;
import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.EventDto;
import com.caplock.booking.service.IEventService;
import com.caplock.booking.service.IWaitListEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventController {
    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String getAllEvents(Model model) {
        model.addAttribute("eventList", eventService.getAllEvents());
        return "events/Events";
    }

    // maybe will be bugged, two same paths
    @GetMapping({"/form", "/form/{id}"})
    public String form(Model model, @PathVariable(required = false) Long id) {
        long safeId = (id == null) ? -1 : id;
        return FormShower.showForm(model, safeId, eventService::getEventById, EventDto.class);
    }

    @PostMapping("/submitForm")
    public String setEvent(@ModelAttribute EventDto event) {
        if (!eventService.setEvent(event)) ;//show error
        return "redirect:/events";
    }
}
