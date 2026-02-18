package com.caplock.booking.util;

import com.caplock.booking.entity.dao.PaymentDAO;
import com.caplock.booking.entity.dto.PaymentDTO;

public class PaymentMapper {
    public static PaymentDTO toDTO(PaymentDAO dao) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(dto.getId());
        dto.setBookingId(dto.getBookingId());
        dto.setStatus(dto.getStatus());
        dto.setPaidAt(dto.getPaidAt());
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
