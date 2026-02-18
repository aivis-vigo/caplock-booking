package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.InvoiceDAO;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InvoiceMockRepository implements IInvoiceRepository{

    private final Map<Long, InvoiceDAO> storage = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public List<InvoiceDAO> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<InvoiceDAO> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public InvoiceDAO save(InvoiceDAO category) {
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
