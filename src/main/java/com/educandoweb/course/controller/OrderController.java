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
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAll() {
        List<Order> orders = orderService.findAll();
        List<OrderDto> dtos = orders.stream().map(OrderDto::new).toList();
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        return ResponseEntity.ok(new OrderDto(order));
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody Order order) {
        Order createdOrder = orderService.create(order);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdOrder.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new OrderDto(createdOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id, @RequestBody Order order) {
        Order updatedOrder = orderService.update(id, order);
        return ResponseEntity.ok(new OrderDto(updatedOrder));
    }
    @PutMapping("/{id}/pay")
    public ResponseEntity<OrderDto> payOrder(@PathVariable Long id){
        Order paidOrder = orderService.processpayment(id);
        return ResponseEntity.ok().body(new OrderDto(paidOrder));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<OrderDto> delete(@PathVariable Long id){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
