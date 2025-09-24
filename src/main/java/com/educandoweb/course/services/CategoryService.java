package com.educandoweb.course.services;

import com.educandoweb.course.entities.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();
    Category findById(Long id);
    Category createCategory(Category obj);
    Category update(Long id, Category obj);
}
