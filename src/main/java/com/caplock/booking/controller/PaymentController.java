package com.caplock.booking.controller;

import com.caplock.booking.entity.dto.PaymentDTO;
import com.caplock.booking.service.IPaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class PaymentController {
    private final IPaymentService service;

    public PaymentController(IPaymentService service) {
        this.service = service;
    }

    @GetMapping
    public List<PaymentDTO> getAll() {
        return service.getAll();
    }

    @PostMapping
    public PaymentDTO create(@RequestBody PaymentDTO dto) {
        return service.create(dto);
    }
}
