package com.educandoweb.course.services;

import com.educandoweb.course.entities.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAll();
    Order findById(Long id);
    Order create(Order order);
    Order processpayment(Long orderId);
    Order update(Long id, Order updatedOrder);
    void delete(Long id);
}
