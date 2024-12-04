package com.educandoweb.course.resources;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.services.OrderService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/orders")
public class OrderResource {

	@Autowired
	private OrderService service;

	@GetMapping
	public ResponseEntity<List<Order>> findAll() {
		List<Order> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Order> findById(@PathVariable Long id) {
		Order obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	@PostMapping(value = "/process")
	public ResponseEntity<Order> processOrder(@RequestBody @Validated Order order){
		Order processedOrder = service.processOrder(order);
		return ResponseEntity.created(URI.create("/orders/" + processedOrder.getId())).body(processedOrder);
	}
	@PostMapping
	public ResponseEntity<Order> create(@Validated @RequestBody Order order){
		Order or = service.create(order);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(or.getId()).toUri();

		return ResponseEntity.created(uri).body(or);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<Order> altered(@Validated @PathVariable Long id, @RequestBody Order obj){
		Order updatedOrder = service.altered(id, obj);
		return ResponseEntity.ok(updatedOrder);
	}
}