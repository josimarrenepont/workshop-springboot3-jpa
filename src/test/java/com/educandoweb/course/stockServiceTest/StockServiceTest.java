package com.educandoweb.course.stockServiceTest;

import com.educandoweb.course.entities.*;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.educandoweb.course.repositories.OrderItemRepository;
import com.educandoweb.course.repositories.OrderRepository;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.services.StockService;
import com.educandoweb.course.services.exceptions.InsufficientStockException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private StockService stockService;

    private Product product;
    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setUp(){

        User user = new User(1L, "user", "user@email.com", "1234567", "1234567");
        order = new Order(1L, Instant.parse("2019-06-20T15:20:01Z"), OrderStatus.PAID, user);
        product = new Product(1L, "Cell Phone", "Iphone 15 pro",
                1500.0, "imgUrl", 7);
        orderItem = new OrderItem();
        orderItem.setQuantity(5);
        orderItem.setProduct(product);
        order.setItems(Collections.singleton(orderItem));
    }
    @Test
    void stockServiceValidateTest(){
        product.setQuantityInStock(10);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> stockService.validateStock(order.getItems()));
    }
    @Test
    void stockServiceValidateTest_throwsException_whenInsufficientStock(){
        product.setQuantityInStock(3);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        InsufficientStockException insufficientStock = assertThrows(InsufficientStockException.class, () ->
                stockService.validateStock(order.getItems()));

        assertEquals("Insufficient stock for product: " + product.getName(),
                insufficientStock.getMessage());
    }
    @Test
    void stockServiceUpdateStockTest(){
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        product.setQuantityInStock(7);
        stockService.updateStock(order.getItems());

        verify(productRepository, times(1)).save(any(Product.class));

        assertEquals(2, product.getQuantityInStock());
    }
}
