package com.caplock.booking.Repository;

import com.caplock.booking.Model.DAO.CategoryDAO;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    List<CategoryDAO> findAll();
    Optional<CategoryDAO> findById(Long id);
    CategoryDAO save(CategoryDAO category);
    void deleteById(Long id);
}
