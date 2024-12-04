package com.educandoweb.course.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.educandoweb.course.entities.Product;
import com.educandoweb.course.services.ProductService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

	@Autowired
	private ProductService service;

	@GetMapping
	public ResponseEntity<List<Product>> findAll() {
		List<Product> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Product> findById(@PathVariable Long id) {
		Product obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	@PostMapping
	public ResponseEntity<Product> insert(@Validated  @RequestBody Product obj){
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<Product> update(@Validated @PathVariable Long id, @RequestBody Product obj){
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@Validated @PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}


}