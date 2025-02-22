package com.educandoweb.course.orderServiceTest;

import com.educandoweb.course.entities.*;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.educandoweb.course.repositories.OrderRepository;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.services.OrderService;
import com.educandoweb.course.services.PaymentService;
import com.educandoweb.course.services.StockService;
import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import com.educandoweb.course.util.OrderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private StockService stockService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

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

        List<Order> result = orderService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).getId());
        verify(orderRepository, times(1)).findAll();
    }
    @Test
    void testFindById(){
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1L);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderStatus(), result.getOrderStatus());

        verify(orderRepository, times(1)).findById(1L);
    }
    @Test
    void testFindById_ResourceNotFound(){
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
           orderService.findById(1L);
        });

        verify(orderRepository, times(1)).findById(1L);
    }
    @Test
    void testCreate(){
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentService.processPayment(any(Order.class))).thenAnswer(invocation -> {
            Order ord = invocation.getArgument(0);
            ord.getPayment().setStatus("APPROVED");
            return ord.getPayment();
        });

        Order result = orderService.create(order);

        assertNotNull(result);
        assertEquals(order.getUser(), result.getUser());
        assertEquals(order.getItems(), result.getItems());
        assertEquals(order.getPayment(), result.getPayment());
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

        Order result = orderService.update(1L, order);

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
    void testDelete(){
        doNothing().when(orderRepository).deleteById(1L);

        orderService.delete(1L);

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
    @Test
    void testProcessOrder(){
        when(orderRepository.save(order)).thenReturn(order);
        when(paymentService.processPayment(order)).thenAnswer(invocation -> {
            Payment pay = order.getPayment();
            pay.setStatus("APPROVED");
            return pay;
        });
        Order result = orderService.processOrder(order);

        assertNotNull(result);
        assertEquals(order.getItems(), result.getItems());
        assertEquals(order.getPayment().getStatus(), "APPROVED");

        verify(stockService, times(1)).validateStock(order.getItems());
        verify(stockService, times(1)).updateStock(order.getItems());
        verify(paymentService, times(1)).processPayment(order);
        verify(orderRepository, times(1)).save(order);
    }
    @Test
    void testProcessOrderInsufficientStock(){
        product.setQuantityInStock(2);
        orderItem.setProduct(product);
        orderItem.setQuantity(5);
        order.getItems().stream().map(OrderItem::getOrder).collect(Collectors.toSet());

        doThrow(new IllegalArgumentException("Insufficient stock for product: " + product.getName()))
                .when(stockService).validateStock(order.getItems());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.processOrder(order));
        assertEquals("Insufficient stock for product: " + product.getName(), exception.getMessage());
    }
}
