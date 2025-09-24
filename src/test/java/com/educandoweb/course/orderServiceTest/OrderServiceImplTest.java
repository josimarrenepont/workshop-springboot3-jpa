package com.educandoweb.course.orderServiceTest;

import com.educandoweb.course.entities.*;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.educandoweb.course.repositories.OrderRepository;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.services.impl.OrderServiceImpl;
import com.educandoweb.course.services.impl.PaymentServiceImpl;
import com.educandoweb.course.services.impl.StockServiceImpl;
import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import com.educandoweb.course.util.OrderUtils;
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
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private StockServiceImpl stockServiceImpl;
    @Mock
    private PaymentServiceImpl paymentServiceImpl;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    private Order order;
    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    void SetUp(){

        User user = new User(1L, "user", "user@email.com", "1234567", "1234567");
        order = new Order(1L, Instant.parse("2019-06-20T15:20:01Z"), OrderStatus.PAID, user);
        product = new Product(1L, "Cell Phone", "Iphone 15 pro",
                1500.0, "imgUrl", 7);
        orderItem = new OrderItem(null, product, 2, product.getPrice());
        order.setItems(Collections.singleton(orderItem));
        Payment payment = new Payment();
        payment.setStatus("PENDING");
        order.setPayment(payment);
    }
    @Test
    void testFindAll(){
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> result = orderServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).getId());
        verify(orderRepository, times(1)).findAll();
    }
    @Test
    void testFindById(){
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderServiceImpl.findById(1L);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderStatus(), result.getOrderStatus());

        verify(orderRepository, times(1)).findById(1L);
    }
    @Test
    void testFindById_ResourceNotFound(){
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
           orderServiceImpl.findById(1L);
        });

        verify(orderRepository, times(1)).findById(1L);
    }
    @Test
    void testCreate(){
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderServiceImpl.create(order);

        assertNotNull(result);
        assertEquals(order.getUser(), result.getUser());
        assertEquals(order.getItems(), result.getItems());
        assertEquals(order.getMoment(), result.getMoment());
        assertEquals(order.getOrderStatus(), result.getOrderStatus());

        double expectedTotal = OrderUtils.calculateTotal(order);
        assertEquals(expectedTotal, result.getTotal());

        verify(orderRepository, times(1)).save(any(Order.class));
    }
    @Test
    void testUpdate(){
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderServiceImpl.update(1L, order);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderStatus(), result.getOrderStatus());
        assertEquals(order.getItems(), result.getItems());
        assertEquals(order.getMoment(), result.getMoment());
        assertEquals(order.getUser(), result.getUser());
        assertEquals(order.getPayment(), result.getPayment());

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    @Test
    void testProcessPaymentSuccessfully() {
        order.setOrderStatus(OrderStatus.PENDING);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(paymentServiceImpl.processPayment(order)).thenReturn(order.getPayment());
        when(orderRepository.save(order)).thenReturn(order);

        Order processedOrder = orderServiceImpl.processpayment(order.getId());

        assertNotNull(processedOrder);
        assertEquals(OrderStatus.PAID, processedOrder.getOrderStatus());

        verify(paymentServiceImpl, times(1)).processPayment(order);
        verify(stockServiceImpl, times(1)).updateStock(order.getItems());
        verify(orderRepository, times(1)).save(order);
    }
    @Test
    void testProcessPaymentAlreadyPaid() {
        order.setOrderStatus(OrderStatus.PAID);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderServiceImpl.processpayment(order.getId()));

        assertEquals("Payment has already been processed or order cancelled.", exception.getMessage());
    }
    @Test
    void testProcessPaymentOrderNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderServiceImpl.processpayment(999L));

        assertEquals("Resource not found. Id 999", exception.getMessage());
    }

    @Test
    void testProcessPaymentFails() {
        order.setOrderStatus(OrderStatus.PENDING);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        doThrow(new RuntimeException("Payment failed")).when(paymentServiceImpl).processPayment(order);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderServiceImpl.processpayment(order.getId()));

        assertEquals("Payment failed", exception.getMessage());
        verify(stockServiceImpl, never()).updateStock(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testDelete(){
        doNothing().when(orderRepository).deleteById(1L);

        orderServiceImpl.delete(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteResourceNotFound(){
        doThrow(ResourceNotFoundException.class).when(orderRepository).deleteById(1L);

        assertThrows(ResourceNotFoundException.class, () -> {
            orderRepository.deleteById(1L);
        });

        verify(orderRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteDatabaseException(){
        doThrow(DatabaseException.class).when(orderRepository).deleteById(1L);

        assertThrows(DatabaseException.class, () -> {
            orderRepository.deleteById(1L);
        });
        verify(orderRepository, times(1)).deleteById(1L);
    }

}
