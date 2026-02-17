package com.caplock.booking.repository;

import com.caplock.booking.entity.DAO.CategoryDAO;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    List<CategoryDAO> findAll();
    Optional<CategoryDAO> findById(Long id);
    CategoryDAO save(CategoryDAO category);
    void deleteById(Long id);
}
