package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.PaymentDAO;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PaymentMockRepository implements IPaymentRepository{
    private final Map<Long, PaymentDAO> storage = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public List<PaymentDAO> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<PaymentDAO> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public PaymentDAO save(PaymentDAO category) {
        if (category.getId() == null) {
            category.setId(idCounter++);
        }
        storage.put(category.getId(), category);
        return category;
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
