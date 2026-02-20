package com.caplock.booking.controller;

import com.caplock.booking.controller.helper.FormShower;
import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.BookingFormDto;
import com.caplock.booking.entity.dto.EventDetailsDto;
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

    @GetMapping("/")
    public String getAllEvents(Model model) {
        model.addAttribute("eventList", eventService.getAllEvents());
        return "events/Events";
    }

    // maybe will be bugged, two same paths
    @GetMapping({"/form", "/form/{id}"})
    public String form(Model model, @PathVariable(required = false) Long id) {
        long safeId = (id == null) ? -1 : id;
        return FormShower.showForm(
                model,
                safeId,
                eventService::getDetails,
                EventDetailsDto.class
        );
    }

    @PostMapping("/submitForm")
    public String setEvent(@ModelAttribute EventDetailsDto event) {
        if (!eventService.setEvent(event)) ;//show error
        return "redirect:/events/";
    }

    @PutMapping("/update/{id}")
    public String putEvent(@ModelAttribute EventDetailsDto event, @PathVariable long id) {
        // get user id from jwt
        if (   eventService.updateEvent(id, event))
            return "redirect:/events/";
        else {
            //show error
            return "redirect:/events/";
        }
    }
    @PostMapping("/delete/{id}")
    public String deleteBooking(@PathVariable long id) {
        eventService.deleteEvent(id);
        return "redirect:/events/";
    }
}
