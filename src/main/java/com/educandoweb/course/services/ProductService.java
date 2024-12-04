package com.educandoweb.course.services;

import java.util.List;
import java.util.Optional;

import com.educandoweb.course.entities.dto.ProductDto;
import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.educandoweb.course.entities.Product;
import com.educandoweb.course.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	public List<Product> findAll(){
		return repository.searchAll();
	}
	
	public Product findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public Product insert(ProductDto obj) {
		Product product = new Product();
		product.setName(obj.getName());
		product.setPrice(obj.getPrice());
		product.setDescription(obj.getDescription());
		product.setImgUrl(obj.getImgUrl());
		product.setQunatityInStock(obj.getQunatityInStock());

		return repository.save(product);
	}

	public Product update(Long id, ProductDto obj) {
		try{
			Product entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return repository.save(entity);
		} catch(EntityNotFoundException e){
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Product entity, ProductDto obj) {
		entity.setName(obj.getName());
		entity.setQunatityInStock(obj.getQunatityInStock());
		entity.setDescription(obj.getDescription());
		entity.setPrice(obj.getPrice());
		entity.setImgUrl(obj.getImgUrl());
	}

	public void delete(Long id) {
		try{
            repository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
			throw new ResourceNotFoundException(id);
		} catch(DataIntegrityViolationException e){
			throw new DatabaseException(e.getMessage());
		}
	}
}
