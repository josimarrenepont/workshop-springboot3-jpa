package com.educandoweb.course.services;

import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.Product;
import com.educandoweb.course.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StockService {
    @Autowired
    private ProductRepository productRepository;

    public void validateStock(Set<OrderItem> items) {
        for (OrderItem item : items) {
            Product product = item.getProduct();
            if (product.getQuantityInStock() < item.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: " + product.getName());
            }
        }
    }
    public void updateStock(Set<OrderItem> items) {
        for (OrderItem item : items) {
            Product product = item.getProduct();
            product.setQuantityInStock(product.getQuantityInStock() - item.getQuantity());
            productRepository.save(product);
        }
    }
}
