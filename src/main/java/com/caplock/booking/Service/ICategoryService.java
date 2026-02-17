package com.caplock.booking.Service;

import com.caplock.booking.Model.DTO.CategoryDTO;

import java.util.List;

public interface ICategoryService {
    List<CategoryDTO> getAll();
    CategoryDTO getById(Long id);
    CategoryDTO create(CategoryDTO dto);
    void delete(Long id);
}
