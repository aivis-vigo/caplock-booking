package com.caplock.booking.Repository;

import com.caplock.booking.Model.DAO.InvoiceDAO;

import java.util.List;
import java.util.Optional;

public interface IInvoiceRepository {
    List<InvoiceDAO> findAll();
    Optional<InvoiceDAO> findById(Long id);
    InvoiceDAO save(InvoiceDAO category);
    void deleteById(Long id);
}
