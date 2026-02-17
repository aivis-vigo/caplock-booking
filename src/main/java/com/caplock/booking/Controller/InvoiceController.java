package com.caplock.booking.Controller;

import com.caplock.booking.Model.DTO.CategoryDTO;
import com.caplock.booking.Model.DTO.InvoiceDTO;
import com.caplock.booking.Service.ICategoryService;
import com.caplock.booking.Service.IInvoiceService;
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
