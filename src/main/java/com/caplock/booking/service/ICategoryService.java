package com.caplock.booking.service;

import com.caplock.booking.entity.dto.CategoryDTO;

import java.util.List;

public interface ICategoryService {
    List<CategoryDTO> getAll();
    CategoryDTO getById(Long id);
    CategoryDTO create(CategoryDTO dto);
    void delete(Long id);
}
