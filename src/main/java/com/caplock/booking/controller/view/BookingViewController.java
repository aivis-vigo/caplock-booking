package com.caplock.booking.controller.view;

import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/bookings")
@RequiredArgsConstructor
public class BookingViewController {
    private final BookingService bookingService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", bookingService.getAll());
        return "ui/bookings/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new BookingDto());
        model.addAttribute("formAction", "/ui/bookings");
        return "ui/bookings/forms/form";
    }

//    @PostMapping
//    public String create(@ModelAttribute("item") BookingDto dto) {
//        bookingService.create(dto, null);
//        return "redirect:/ui/bookings";
//    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        BookingDto dto = bookingService.getById(id).orElseThrow();
        model.addAttribute("item", dto);
        model.addAttribute("formAction", "/ui/bookings/" + id);
        return "ui/bookings/forms/form";
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
