package com.educandoweb.course.services;

import com.educandoweb.course.entities.Product;
import com.educandoweb.course.entities.dto.ProductDto;
import java.util.List;


public interface ProductService {

    List<Product> findAll();
    Product findById(Long id);
    Product insert(ProductDto obj);
    Product update(Long id, ProductDto productDto);
    void delete(Long id);
}
