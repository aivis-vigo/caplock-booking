package com.caplock.booking.Mappers;

import com.caplock.booking.Model.DAO.CategoryDAO;
import com.caplock.booking.Model.DAO.PaymentDAO;
import com.caplock.booking.Model.DTO.CategoryDTO;
import com.caplock.booking.Model.DTO.PaymentDTO;

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
