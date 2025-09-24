package com.educandoweb.course.services.impl;

import java.util.List;
import java.util.Optional;

import com.educandoweb.course.services.CategoryService;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import com.educandoweb.course.entities.Category;
import com.educandoweb.course.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {


	private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
	@Override
    public List<Category> findAll(){
		return categoryRepository.searchAll();
	}
	@Override
	public Category findById(Long id) {
		Optional<Category> obj = categoryRepository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	@Override
	public Category createCategory(Category obj) {
		return categoryRepository.save(obj);
	}
	@Override
	public Category update(Long id, Category obj) {
		try{
			Category entity = categoryRepository.getReferenceById(id);
			updateData(entity, obj);
			return categoryRepository.save(entity);
		} catch(EntityNotFoundException e){
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Category entity, Category obj) {
		entity.setName(obj.getName());
	}
}
