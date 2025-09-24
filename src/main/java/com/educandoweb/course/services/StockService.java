package com.educandoweb.course.services;

import com.educandoweb.course.entities.OrderItem;

import java.util.Set;

public interface StockService {

    void validateStock(Set<OrderItem> items);
    void updateStock(Set<OrderItem> items);
}