package com.educandoweb.course.controller;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.dto.ProductDto;
import com.educandoweb.course.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.educandoweb.course.entities.Product;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "Products", description = "Endpoints deProdutos")
@RestController
@RequestMapping(value = "/products")
public class ProductController {

	private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

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
	@Operation(summary = "Criar produto", description = "Cadastro de Produtos")
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
	@GetMapping(value = "/{productId}/orders")
	public ResponseEntity<Set<Order>> findOrdersByProductId(@PathVariable Long productId){
		Set<Order> orders = service.findOrdersByProductId(productId);
		return ResponseEntity.ok().body(orders);
	}
}