package com.caplock.booking.repository.old.jpa;

import com.caplock.booking.entity.old.dao.PaymentDAO;

import java.util.List;
import java.util.Optional;

public interface IPaymentRepository {
    List<PaymentDAO> findAll();
    Optional<PaymentDAO> findById(Long id);
    PaymentDAO save(PaymentDAO category);
    void deleteById(Long id);
}
