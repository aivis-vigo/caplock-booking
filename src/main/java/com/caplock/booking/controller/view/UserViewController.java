package com.caplock.booking.controller.view;

import com.caplock.booking.entity.dto.UserDto;
import com.caplock.booking.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/users")
@RequiredArgsConstructor
public class UserViewController {
    private final IUserService userService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", userService.getAll());
        return "ui/users/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new UserDto());
        model.addAttribute("formAction", "/ui/users");
        return "ui/users/form";
    }

    @PostMapping
    public String create(@ModelAttribute("item") UserDto dto) {
        userService.create(dto);
        return "redirect:/ui/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        UserDto dto = userService.getById(id).orElseThrow();
        model.addAttribute("item", dto);
        model.addAttribute("formAction", "/ui/users/" + id);
        return "ui/users/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("item") UserDto dto) {
        userService.update(id, dto);
        return "redirect:/ui/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/ui/users";
    }
}
