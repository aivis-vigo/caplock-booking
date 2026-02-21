package com.caplock.booking.controller.old;

import com.caplock.booking.controller.old.helper.FormShower;
import com.caplock.booking.entity.old.dto.EventDto;
import com.caplock.booking.service.old.IEventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/events/details")
public class EventDetailsController {

    private final IEventService eventService;

    public EventDetailsController(IEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/view/{id}")
    public String getDetails(@PathVariable long id, Model model) {
        model.addAttribute("eventList", List.of(eventService.getDetails(id)));
        return "events/eventDetails";
    }

    @PostMapping("/delete/{id}")
    public String deleteBooking(@PathVariable long id) {
        eventService.deleteEvent(id);
        return "redirect:/events/";
    }

    @GetMapping({"/form", "/form/{id}"})
    public String form(Model model, @PathVariable(required = false) Long id) {
        long safeId = (id == null) ? -1 : id;
        return FormShower.showForm(
                model,
                safeId,
                eventService::getEventById,
                EventDto.class
        );
    }


}
