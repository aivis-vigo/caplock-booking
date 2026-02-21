package com.caplock.booking.controller.old;

import com.caplock.booking.controller.old.helper.FormShower;
import com.caplock.booking.entity.old.dto.WaitListEntryDto;
import com.caplock.booking.service.old.IWaitListEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/waitList")
public class WaitListEntryController {
    private final IWaitListEntryService waitListEntryService;

    public WaitListEntryController(IWaitListEntryService WaitListEntryService) {
        this.waitListEntryService = WaitListEntryService;
    }

    @GetMapping({"/form", "/form/{id}"})
    public String form(Model model, @PathVariable(required = false) Long id) {
        long safeId = (id == null) ? -1 : id;
        return FormShower.showForm(
                model,
                safeId,
                waitListEntryService::getAllWaitListById,
                WaitListEntryDto.class
        );
    }

    @GetMapping("/{userId}")
    public String getAllWaitLists(Model model, @PathVariable long userId) {
        // get user id from jwt
        model.addAttribute("waitList-list", waitListEntryService.getAllWaitListByUser(userId));
        return "waitList/waitList";
    }



    @PostMapping("/submitForm")
    public String setEvent(@ModelAttribute WaitListEntryDto w) {
        //get user using jwt
        long userId = -1;
        waitListEntryService.setWaitListToUser(userId, w);
        return "redirect:/bookings/";
    }

}
