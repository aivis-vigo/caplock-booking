package com.caplock.booking.controller;

import com.caplock.booking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}")
    public String details(@PathVariable Integer id, Model model) {
        UserService userService = new UserService();

        model.addAttribute("user", userService.getUserById(id));

        // refers to resources/templates/index.html
        return "users/details";
    }

}
