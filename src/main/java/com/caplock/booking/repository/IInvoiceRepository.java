package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.InvoiceDAO;

import java.util.List;
import java.util.Optional;

public interface IInvoiceRepository {
    List<InvoiceDAO> findAll();
    Optional<InvoiceDAO> findById(Long id);
    InvoiceDAO save(InvoiceDAO category);
    void deleteById(Long id);
}
