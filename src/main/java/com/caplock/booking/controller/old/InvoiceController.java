package com.caplock.booking.controller.old;

import com.caplock.booking.entity.old.dto.InvoiceDTO;
import com.caplock.booking.service.old.IInvoiceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class InvoiceController {
    private final IInvoiceService service;

    public InvoiceController(IInvoiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<InvoiceDTO> getAll() {
        return service.getAll();
    }

    @PostMapping
    public InvoiceDTO create(@RequestBody InvoiceDTO dto) {
        return service.create(dto);
    }
}
