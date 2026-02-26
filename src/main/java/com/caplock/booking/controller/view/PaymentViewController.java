package com.caplock.booking.controller.view;

import com.caplock.booking.entity.dto.PaymentDto;
import com.caplock.booking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/payments")
@RequiredArgsConstructor
public class PaymentViewController {
    private final PaymentService paymentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", paymentService.getAll());
        return "ui/payments/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new PaymentDto());
        model.addAttribute("formAction", "/ui/payments");
        return "ui/payments/form";
    }

    @PostMapping
    public String create(@ModelAttribute("item") PaymentDto dto) {
        paymentService.create(dto);
        return "redirect:/ui/payments";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        PaymentDto dto = paymentService.getById(id).orElseThrow();
        model.addAttribute("item", dto);
        model.addAttribute("formAction", "/ui/payments/" + id);
        return "ui/payments/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("item") PaymentDto dto) {
        paymentService.update(id, dto);
        return "redirect:/ui/payments";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        paymentService.delete(id);
        return "redirect:/ui/payments";
    }
}
