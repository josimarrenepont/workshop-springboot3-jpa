package com.educandoweb.course.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.educandoweb.course.entities.Category;
import com.educandoweb.course.repositories.CategoryRepository;
import com.educandoweb.course.services.CategoryService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService service;
	
	@Autowired
	private CategoryRepository repository;

	@GetMapping
	public ResponseEntity<List<Category>> findAll() {
		List<Category> list = repository.searchAll();
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Category> findById(@PathVariable Long id) {
		Category obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	@PostMapping
	public ResponseEntity<Category> createCategory(@Validated @RequestBody Category obj){
		Category category = service.createCategory(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(category.getId()).toUri();

		return ResponseEntity.created(uri).body(category);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<Category> update(@Validated @PathVariable Long id, @RequestBody Category obj){
		Category updatedCategory = service.update(id, obj);
		return ResponseEntity.ok().body(updatedCategory);
	}
}