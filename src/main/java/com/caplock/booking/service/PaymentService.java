package com.caplock.booking.service;

import com.caplock.booking.entity.dto.PaymentDto;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    PaymentDto create(PaymentDto dto);

    Optional<PaymentDto> getById(Long id);

    List<PaymentDto> getAll();

    PaymentDto update(Long id, PaymentDto dto);

    void delete(Long id);
}
