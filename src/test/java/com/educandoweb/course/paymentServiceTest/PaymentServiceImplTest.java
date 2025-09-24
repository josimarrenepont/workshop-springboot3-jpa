package com.educandoweb.course.paymentServiceTest;

import com.educandoweb.course.entities.*;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.educandoweb.course.repositories.OrderItemRepository;
import com.educandoweb.course.repositories.OrderRepository;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.services.impl.PaymentServiceImpl;
import com.educandoweb.course.services.impl.StockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private StockServiceImpl stockServiceImpl;

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    private Order order;
    private Payment payment;
    private OrderItem orderItem;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        User user = new User(1L, "user", "user@email.com", "1234567", "1234567");
        order = new Order(1L, Instant.parse("2019-06-20T15:20:01Z"), OrderStatus.PAID, user);
        Product product = new Product(1L, "Cell Phone", "Iphone 15 pro",
                1500.0, "imgUrl", 7);
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(5);
        orderItem.setProduct(product);
        order.setItems(Collections.singleton(orderItem));
        Payment payment = new Payment();
        payment.setStatus("PENDING");
        order.setPayment(payment);
    }
    @Test
    void testProcessPayment(){
        doNothing().when(stockServiceImpl).validateStock(order.getItems());

        when(orderRepository.save(order)).thenReturn(order);

        Payment processedPayment = paymentServiceImpl.processPayment(order);

        assertNotNull(processedPayment);
        assertEquals(processedPayment.getStatus(), "APPROVED");
        assertTrue(processedPayment.getTransactionId().startsWith("TXN"));

        verify(orderRepository, times(1)).save(order);
    }
}