package com.caplock.booking.controller.view;

import com.caplock.booking.entity.dto.InvoiceDto;
import com.caplock.booking.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/invoices")
@RequiredArgsConstructor
public class InvoiceViewController {
    private final InvoiceService invoiceService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", invoiceService.getAll());
        return "ui/invoices/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new InvoiceDto());
        model.addAttribute("formAction", "/ui/invoices");
        return "ui/invoices/forms/form";
    }

    @PostMapping
    public String create(@ModelAttribute("item") InvoiceDto dto) {
        invoiceService.create(dto);
        return "redirect:/ui/invoices";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        InvoiceDto dto = invoiceService.getById(id).orElseThrow();
        model.addAttribute("item", dto);
        model.addAttribute("formAction", "/ui/invoices/" + id);
        return "ui/invoices/forms/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("item") InvoiceDto dto) {
        invoiceService.update(id, dto);
        return "redirect:/ui/invoices";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        invoiceService.delete(id);
        return "redirect:/ui/invoices";
    }
}
