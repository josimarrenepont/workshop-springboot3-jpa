package com.educandoweb.course.productsServiceTest;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.Product;
import com.educandoweb.course.entities.dto.ProductDto;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.services.ProductService;
import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hibernate.internal.util.collections.CollectionHelper.setOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;
    private OrderItem orderItem;

    @BeforeEach
    public void setUp(){
        product = new Product(1L, "Cell Phone", "Iphone 15 pro",
                1500.0, "imgUrl", 7);
        productDto = new ProductDto(1L, "Cell Phone", "Iphone 15 pro", 1500.0, "imgUrl", 7);
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testFindAll(){

        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> result = productService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(product.getId(), result.get(0).getId());
        assertEquals(product.getName(), result.get(0).getName());

        verify(productRepository, times(1)).findAll();
    }
    @Test
    void testFindById(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());;
        assertEquals(product.getName(), result.getName());

        verify(productRepository, times(1)).findById(1L);
    }
    @Test
    void findById_ResourceNotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(1L);
        });
        verify(productRepository, times(1)).findById(1L);
    }
    @Test
    void testInsert(){
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.insert(new ProductDto(product));

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());

        verify(productRepository, times(1)).save(any(Product.class));

    }
    @Test
    void testUpdate(){
        when(productRepository.getReferenceById(1L)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.update(1L, productDto);

        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());

        verify(productRepository, times(1)).getReferenceById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }
    @Test
    void testDelete(){
        doNothing().when(productRepository).deleteById(1L);

        productService.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteResourceNotFoundException(){
        doThrow(ResourceNotFoundException.class).when(productRepository).deleteById(1L);

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(1L);
        });

        verify(productRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteDatabaseException(){
        doThrow(DatabaseException.class).when(productRepository).deleteById(1L);

        assertThrows(DatabaseException.class, () -> {
            productService.delete(1L);
        });

        verify(productRepository, times(1)).deleteById(1L);
    }
    @Test
    void testFindOrdersByProductId(){
        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order2.setId(2L);

        OrderItem orderItem1 = new OrderItem(order1, product, 7, 10.0);
        OrderItem orderItem2 = new OrderItem(order2, product, 7, 10.0);

        Set<OrderItem> items = setOf(orderItem1, orderItem2);
        product.setItems(items);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Set<Order> result = productService.findOrdersByProductId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order2));

        verify(productRepository, times(1)).findById(1L);
    }
    @Test
    void testFindOrdersByProductId_ResourceNotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findOrdersByProductId(1L);
        });

        verify(productRepository, times(1)).findById(1L);
    }
}
