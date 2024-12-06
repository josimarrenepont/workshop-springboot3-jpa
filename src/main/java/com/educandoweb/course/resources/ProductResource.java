package com.educandoweb.course.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.educandoweb.course.entities.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.educandoweb.course.entities.Product;
import com.educandoweb.course.services.ProductService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/api/products")
public class ProductResource {

	@Autowired
	private ProductService service;

	@GetMapping
	public ResponseEntity<List<ProductDto>> findAll() {
		List<Product> list = service.findAll();
		List<ProductDto> dtos = list.stream().map(ProductDto::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(dtos);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> findById(@PathVariable Long id) {
		Product obj = service.findById(id);
		ProductDto productDto = new ProductDto(obj);
		return ResponseEntity.ok().body(productDto);
	}
	@PostMapping
	public ResponseEntity<ProductDto> insert(@Validated  @RequestBody ProductDto dto){
		Product product = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).body(new ProductDto(product));
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDto> update(@Validated @PathVariable Long id, @RequestBody ProductDto dto){
		Product obj = service.update(id, dto);
		ProductDto productDto = new ProductDto(obj);
		return ResponseEntity.ok().body(productDto);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@Validated @PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}


}