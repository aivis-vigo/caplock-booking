package com.caplock.booking.controller;

import com.caplock.booking.dto.UserCreationDTO;
import com.caplock.booking.dto.UserDTOMapper;
import com.caplock.booking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    UserService userService;

    public UserController() {
        this.userService = new UserService(new UserDTOMapper());
    }

    @GetMapping("/")
    public String index() {
        return "users/index";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.getUserById(id));

        // refers to resources/templates/users/index.html
        return "users/details";
    }

    @GetMapping("/all")
    public String allUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        // refers to resources/templates/users/list.html
        return "users/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        // refers to resources/templates/users/create.html
        return "users/create";
    }

    // @ModelAttribute - binds form data into object model
    @PostMapping("/submit-form")
    public String createUser(@ModelAttribute UserCreationDTO user) {
        userService.saveUser(user);
        return "redirect:/user/all";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "users/edit";
    }

    // @ModelAttribute - binds form data into object model
    @PutMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute UserCreationDTO user) {
        userService.updateUser(id, user);
        return "redirect:/user/all";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return "redirect:/user/all";
    }
}
