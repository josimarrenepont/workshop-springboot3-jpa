package com.educandoweb.course.orderControllerTest;

import com.educandoweb.course.controller.OrderController;
import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.entities.dto.OrderDto;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.educandoweb.course.services.impl.OrderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderServiceImpl orderServiceImpl;

    @InjectMocks
    private OrderController orderController;

    private Order order;
    private User user;
    private OrderItem orderItem;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice()
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        user = new User(1L, "user", "user@email.com", "1234567", "1234567");
        order = new Order(1L, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.PAID, user);
    }

    @Test
    void testFindAll() throws Exception {
        List<OrderDto> orderDtoList = Collections.singletonList(new OrderDto(order));
        when(orderServiceImpl.findAll()).thenReturn(List.of(order));

        ResultActions result = mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[0].moment").value(order.getMoment().toString()))
                .andExpect(jsonPath("$[0].orderStatus").value(order.getOrderStatus().toString()));
    }
    @Test
    void testFindById() throws Exception{
        when(orderServiceImpl.findById(1L)).thenReturn(order);

        ResultActions result = mockMvc.perform(get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.moment").value(order.getMoment().toString()))
                .andExpect(jsonPath("$.orderStatus").value(order.getOrderStatus().toString()));
    }
    @Test
    void testCreate() throws Exception{

        when(orderServiceImpl.create(any(Order.class))).thenReturn(order);

        String orderJson = """
                {
                    "moment": "2019-06-20T19:53:07Z",
                    "orderStatus": "PAID",
                    "user": {"id": 1}
                }
                """;

        ResultActions result = mockMvc.perform(post("/orders")
                        .content(orderJson)
            .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.moment").value(order.getMoment().toString()))
                .andExpect(jsonPath("$.orderStatus").value(order.getOrderStatus().toString()));
    }
    @Test
    void testPayOrderEndpoint() throws Exception {
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderStatus(OrderStatus.PAID);
        when(orderServiceImpl.processpayment(order.getId())).thenReturn(order);

        ResultActions result = mockMvc.perform(put("/orders/{id}/pay", order.getId())
                .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderStatus").value("PAID"));

        verify(orderServiceImpl, times(1)).processpayment(order.getId());
    }
    @Test
    void testUpdate() throws Exception{
        Order order = new Order(1L, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.PAID, user);
        OrderDto orderDto = new OrderDto(order);
        when(orderServiceImpl.update(eq(1L), any(Order.class))).thenReturn(order);

        String orderJson = """
                {
                    "moment": "2019-06-20T19:53:07Z",
                    "orderStatus": "PAID",
                    "user": {"id": 1}
                }
                """;

        ResultActions result = mockMvc.perform(put("/orders/1")
                .content(orderJson)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.moment").value(order.getMoment().toString()))
                .andExpect(jsonPath("$.orderStatus").value(order.getOrderStatus().toString()));
    }
    @Test
    void testDelete() throws Exception{
        Long orderId = 1L;
        doNothing().when(orderServiceImpl).delete(orderId);

        ResultActions result = mockMvc.perform(delete("/orders/1", orderId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
        verify(orderServiceImpl, times(1)).delete(orderId);
    }
}
