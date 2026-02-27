package com.caplock.booking.controller.view;

import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.BookingEventRowDto;
import com.caplock.booking.service.BookingService;
import com.caplock.booking.service.EventService;
import com.caplock.booking.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/bookings")
@RequiredArgsConstructor
public class BookingViewController {
    private final BookingService bookingService;
    private final EventService eventService;
    private final UserService userService;

    @GetMapping
    public String list(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userEmail = auth.getName();
        long userId = userService.getUserIdByEmail(userEmail);
        var bookingDtos = bookingService.getAll().stream().filter(x -> x.getUserId() == userId).toList();

        var beObjects = new ArrayList<BookingEventRowDto>();

        for (var bookingDto : bookingDtos) {
            var eventDto = eventService.getEventDetailsByEventId(bookingDto.getEventId()).orElseThrow().getEvent();
            beObjects.add(new BookingEventRowDto(bookingDto, eventDto, userEmail));
        }

        model.addAttribute("items", beObjects);
        return "ui/bookings/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new BookingDto());
        model.addAttribute("formAction", "/ui/bookings");
        return "ui/bookings/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        BookingDto dto = bookingService.getById(id).orElseThrow();
        model.addAttribute("item", dto);
        model.addAttribute("formAction", "/ui/bookings/" + id);
        return "ui/bookings/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("item") BookingDto dto) {
        bookingService.update(id, dto);
        return "redirect:/ui/bookings";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        bookingService.delete(id);
        return "redirect:/ui/bookings";
    }
}
