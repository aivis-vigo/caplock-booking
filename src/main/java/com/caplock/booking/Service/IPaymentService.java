package com.caplock.booking.Service;

import com.caplock.booking.Model.DAO.PaymentDAO;
import com.caplock.booking.Model.DTO.PaymentDTO;

import java.util.List;

public interface IPaymentService {
    List<PaymentDTO> getAll();
    PaymentDTO getById(Long id);
    PaymentDTO create(PaymentDTO dto);
    void delete(Long id);
}
