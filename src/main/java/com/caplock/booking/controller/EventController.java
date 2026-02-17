package com.caplock.booking.controller;

import com.caplock.booking.service.IEventService;
import com.caplock.booking.service.IWaitListEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("event/")
public class EventController {
    private final IEventService EventService;

    public EventController(IEventService iEventService) {
        this.EventService = iEventService;
    }
}
