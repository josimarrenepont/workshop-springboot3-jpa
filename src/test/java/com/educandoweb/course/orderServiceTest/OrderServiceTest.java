package com.educandoweb.course.orderServiceTest;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.Product;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.educandoweb.course.repositories.OrderRepository;
import com.educandoweb.course.services.OrderService;
import com.educandoweb.course.services.PaymentService;
import com.educandoweb.course.services.StockService;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository repository;
    @Mock
    private StockService stockService;
    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private User user;
    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    void SetUp(){
        user = new User(1L, "user", "user@email.com", "1234567", "1234567");
        order = new Order(1L, Instant.parse("2019-06-20T15:20:01Z"), OrderStatus.PAID, user);
        product = new Product(1L, "Cell Phone", "Iphone 15 pro",
                1500.0, "imgUrl", 7);
        orderItem = new OrderItem(null, product, 2, product.getPrice());
        order.setItems(Collections.singleton(orderItem));
    }
    @Test
    void testFindAll(){
        when(repository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> result = orderService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).getId());
        verify(repository, times(1)).findAll();
    }
    @Test
    void testFindById(){
        when(repository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1L);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderStatus(), result.getOrderStatus());

        verify(repository, times(1)).findById(1L);
    }
    @Test
    void testFindById_ResourceNotFound(){
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
           orderService.findById(1L);
        });

        verify(repository, times(1)).findById(1L);
    }
    @Test
    void testCreate(){
        when(repository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.create(order);

        assertNotNull(result);
        assertEquals(order.getClient(), result.getClient());
        assertEquals(order.getItems(), result.getItems());
        assertEquals(order.getPayment(), result.getPayment());
        assertEquals(order.getMoment(), result.getMoment());
        assertEquals(order.getOrderStatus(), result.getOrderStatus());

        verify(repository, times(1)).save(any(Order.class));
    }
    @Test
    void testUpdate(){
        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(repository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.update(1L, order);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderStatus(), result.getOrderStatus());
        assertEquals(order.getItems(), result.getItems());
        assertEquals(order.getMoment(), result.getMoment());
        assertEquals(order.getClient(), result.getClient());
        assertEquals(order.getPayment(), result.getPayment());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Order.class));
    }
}
