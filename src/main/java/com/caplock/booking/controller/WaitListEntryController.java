package com.caplock.booking.controller;

import com.caplock.booking.service.IWaitListEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("waitlist/")
public class WaitListEntryController {
    private final IWaitListEntryService WaitListEntryService;

    public WaitListEntryController(IWaitListEntryService WaitListEntryService) {
        this.iWaitListEntryService = iWaitListEntryService;
    }
}
