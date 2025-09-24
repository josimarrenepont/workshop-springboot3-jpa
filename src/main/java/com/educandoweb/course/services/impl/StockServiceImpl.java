package com.educandoweb.course.services.impl;

import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.Product;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.services.StockService;
import com.educandoweb.course.services.exceptions.InsufficientStockException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import com.educandoweb.course.services.exceptions.StockUpdateException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StockServiceImpl implements StockService {

    private final ProductRepository productRepository;

    public StockServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public void validateStock(Set<OrderItem> items) {
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException
                            ("Product not found for id: " + item.getProduct().getId()));
            if (product.getQuantityInStock() < item.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName());
            }
        }
    }
    @Override
    public void updateStock(Set<OrderItem> items) {
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException
                            ("Product not found for id " + item.getProduct().getId()));

            if(product.getQuantityInStock() < item.getQuantity()){
                throw new InsufficientStockException("Insufficient stock for product " + product.getName());
            }

            product.setQuantityInStock(product.getQuantityInStock() - item.getQuantity());
            try {
                productRepository.save(product);
            } catch (Exception e){
                throw new StockUpdateException("Error updating stock for product " + product.getName(), e);
            }
        }
    }
}
