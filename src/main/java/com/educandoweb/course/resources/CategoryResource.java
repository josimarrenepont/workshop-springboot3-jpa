package com.educandoweb.course.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educandoweb.course.entities.Category;
import com.educandoweb.course.repositories.CategoryRepository;
import com.educandoweb.course.services.CategoryService;

@RestController //
@RequestMapping(value = "/categories") //
public class CategoryResource {

	@Autowired
	private CategoryService service;
	
	@Autowired
	private CategoryRepository repository;

	@GetMapping
	public ResponseEntity<List<Category>> findAll() { // VERIFICAR SE A CONEXÃO ESTÁ FUNCIONANDO
		List<Category> list = repository.searchAll();
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Category> findById(@PathVariable Long id) {
		Category obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
}