package com.caplock.booking.service.old;

import com.caplock.booking.entity.old.dto.CategoryDTO;

import java.util.List;

public interface ICategoryService {
    List<CategoryDTO> getAll();
    CategoryDTO getById(Long id);
    CategoryDTO create(CategoryDTO dto);
    void delete(Long id);
}
