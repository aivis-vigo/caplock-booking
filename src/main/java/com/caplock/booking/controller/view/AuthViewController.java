package com.caplock.booking.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/auth")
public class AuthViewController {

    @GetMapping("/login")
    public String login() {
        return "ui/auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "/ui/auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "ui/auth/register";
    }
}
