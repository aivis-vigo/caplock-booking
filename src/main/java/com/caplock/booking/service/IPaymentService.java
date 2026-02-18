package com.caplock.booking.service;

import com.caplock.booking.entity.dto.PaymentDTO;

import java.util.List;

public interface IPaymentService {
    List<PaymentDTO> getAll();
    PaymentDTO getById(Long id);
    PaymentDTO create(PaymentDTO dto);
    void delete(Long id);
}
