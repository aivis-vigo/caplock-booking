package com.caplock.booking.Repository;

import com.caplock.booking.Model.DAO.PaymentDAO;

import java.util.List;
import java.util.Optional;

public interface IPaymentRepository {
    List<PaymentDAO> findAll();
    Optional<PaymentDAO> findById(Long id);
    PaymentDAO save(PaymentDAO category);
    void deleteById(Long id);
}
