package com.caplock.booking.service.old;

import com.caplock.booking.entity.old.dto.PaymentDTO;

import java.util.List;

public interface IPaymentService {
    List<PaymentDTO> getAll();
    PaymentDTO getById(Long id);
    PaymentDTO create(PaymentDTO dto);
    void delete(Long id);
}
