package com.caplock.booking.util;

import com.caplock.booking.entity.dao.InvoiceDAO;
import com.caplock.booking.entity.dto.InvoiceDTO;

public class InvoiceMapper {
    public static InvoiceDTO toDTO(InvoiceDAO dao) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setAmount(dao.getAmount());
        dto.setId(dao.getId());
        dto.setCreatedAt(dto.getCreatedAt());
        dto.setInvoiceNumber(dao.getInvoiceNumber());
        return dto;
    }

    public static InvoiceDAO toDAO(InvoiceDTO invoiceDTO) {
        InvoiceDAO dao = new InvoiceDAO();
        dao.setId(invoiceDTO.getId());
        dao.setAmount(invoiceDTO.getAmount());
        dao.setCreatedAt(invoiceDTO.getCreatedAt());
        dao.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        return dao;
    }
}
