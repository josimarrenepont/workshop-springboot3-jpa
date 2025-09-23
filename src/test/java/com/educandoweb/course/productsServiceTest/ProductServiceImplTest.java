package com.educandoweb.course.productsServiceTest;

import com.educandoweb.course.entities.Product;
import com.educandoweb.course.entities.dto.ProductDto;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.services.impl.ProductServiceImpl;
import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

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

        when(productRepository.searchAll()).thenReturn(List.of(product));

        List<Product> result = productServiceImpl.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(product.getId(), result.get(0).getId());
        assertEquals(product.getName(), result.get(0).getName());

        verify(productRepository, times(1)).searchAll();
    }
    @Test
    void testFindById(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productServiceImpl.findById(1L);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());;
        assertEquals(product.getName(), result.getName());

        verify(productRepository, times(1)).findById(1L);
    }
    @Test
    void findById_ResourceNotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productServiceImpl.findById(1L);
        });
        verify(productRepository, times(1)).findById(1L);
    }
    @Test
    void testInsert(){
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productServiceImpl.insert(new ProductDto(product));

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());

        verify(productRepository, times(1)).save(any(Product.class));

    }
    @Test
    void testUpdate(){
        when(productRepository.getReferenceById(1L)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productServiceImpl.update(1L, productDto);

        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());

        verify(productRepository, times(1)).getReferenceById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }
    @Test
    void testDelete(){
        doNothing().when(productRepository).deleteById(1L);

        productServiceImpl.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteResourceNotFoundException(){
        doThrow(ResourceNotFoundException.class).when(productRepository).deleteById(1L);

        assertThrows(ResourceNotFoundException.class, () -> {
            productServiceImpl.delete(1L);
        });

        verify(productRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteDatabaseException(){
        doThrow(DatabaseException.class).when(productRepository).deleteById(1L);

        assertThrows(DatabaseException.class, () -> {
            productServiceImpl.delete(1L);
        });

        verify(productRepository, times(1)).deleteById(1L);
    }
}
