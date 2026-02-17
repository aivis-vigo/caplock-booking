package com.caplock.booking.mappers;

import com.caplock.booking.entity.DAO.CategoryDAO;
import com.caplock.booking.entity.DTO.CategoryDTO;

public class CategoryMapper {
    public static CategoryDTO toDTO(CategoryDAO dao) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(dao.getId());
        dto.setName(dao.getName());
        dto.setDescription(dao.getDescription());
        return dto;
    }

    public static CategoryDAO toDAO(CategoryDTO dto) {
        CategoryDAO dao = new CategoryDAO();
        dao.setId(dto.getId());
        dao.setName(dto.getName());
        dao.setDescription(dto.getDescription());
        return dao;
    }
}
