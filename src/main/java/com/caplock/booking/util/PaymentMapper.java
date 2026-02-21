package com.caplock.booking.util;

import com.caplock.booking.entity.dao.PaymentDAO;
import com.caplock.booking.entity.dto.PaymentDTO;

public class PaymentMapper {
    public static PaymentDTO toDTO(PaymentDAO dao) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(dao.getId());
        dto.setBookingId(dao.getBookingId());
        dto.setStatus(dao.getStatus());
        dto.setAmount(dao.getAmount());
        dto.setPaymentMethod(dao.getPaymentMethod());
        dto.setPaidAt(dao.getPaidAt());
        return dto;
    }

    public static PaymentDAO toDAO(PaymentDTO dto) {
        PaymentDAO dao = new PaymentDAO();
        dao.setId(dto.getId());
        dao.setBookingId(dto.getBookingId());
        dao.setStatus(dto.getStatus());
        dao.setPaidAt(dto.getPaidAt());
        return dao;
    }
}
