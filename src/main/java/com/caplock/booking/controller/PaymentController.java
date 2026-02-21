package com.caplock.booking.controller;

import com.caplock.booking.entity.dto.PaymentDTO;
import com.caplock.booking.service.IPaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    private final IPaymentService service;

    public PaymentController(IPaymentService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<PaymentDTO> getAll() {
        return service.getAll();
    }

    @PostMapping("/create/")
    public PaymentDTO create(@RequestBody PaymentDTO dto) {
        return service.create(dto);
    }
}
