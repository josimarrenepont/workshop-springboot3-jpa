package com.educandoweb.course.controller;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.dto.OrderDto;
import com.educandoweb.course.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAll() {
        List<Order> orders = service.findAll();
        List<OrderDto> dtos = orders.stream().map(OrderDto::new).toList();
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id) {
        Order order = service.findById(id);
        return ResponseEntity.ok(new OrderDto(order));
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody Order order) {
        Order createdOrder = service.create(order);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdOrder.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new OrderDto(createdOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id, @RequestBody Order order) {
        Order updatedOrder = service.update(id, order);
        return ResponseEntity.ok(new OrderDto(updatedOrder));
    }

    @PostMapping(value = "/process")
    public ResponseEntity<OrderDto> processOrder(@RequestBody Order order) {
        Order processedOrder = service.processOrder(order);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(processedOrder.getId()).toUri();
        return ResponseEntity.created(uri).body(new OrderDto(processedOrder));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<OrderDto> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
