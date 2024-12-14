package com.educandoweb.course.productsServiceTest;

import com.educandoweb.course.entities.Product;
import com.educandoweb.course.entities.dto.ProductDto;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.services.ProductService;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    public void setUp(){
        product = new Product(1L, "Cell Phone", "Iphone 15 pro",
                1500.0, "imgUrl", 7);
        productDto = new ProductDto(1L, "Cell Phone", "Iphone 15 pro", 1500.0, "imgUrl", 7);
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testFindAll(){

        when(repository.searchAll()).thenReturn(List.of(product));

        List<Product> result = productService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(product.getId(), result.get(0).getId());
        assertEquals(product.getName(), result.get(0).getName());

        verify(repository, times(1)).searchAll();
    }
    @Test
    void testFindById(){
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());;
        assertEquals(product.getName(), result.getName());

        verify(repository, times(1)).findById(1L);
    }
    @Test
    void findById_ResourceNotFound(){
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(1L);
        });
        verify(repository, times(1)).findById(1L);
    }
    @Test
    void testInsert(){
        when(repository.save(any(Product.class))).thenReturn(product);

        Product result = productService.insert(new ProductDto(product));

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());

        verify(repository, times(1)).save(any(Product.class));

    }
    @Test
    void testUpdate(){
        when(repository.getReferenceById(1L)).thenReturn(product);
        when(repository.save(any(Product.class))).thenReturn(product);

        Product result = productService.update(1L, productDto);

        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());

        verify(repository, times(1)).getReferenceById(1L);
        verify(repository, times(1)).save(any(Product.class));
    }
    @Test
    void testDelete(){
        doNothing().when(repository).deleteById(1L);

        productService.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
