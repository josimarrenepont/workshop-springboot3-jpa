package com.educandoweb.course.entities.dto;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.enums.OrderStatus;

import java.time.Instant;

public class OrderDto {
    private Long id;
    private Instant moment;
    private OrderStatus orderStatus;
    private Double total;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.moment = order.getMoment();
        this.orderStatus = order.getOrderStatus();
        this.total = order.getItems().stream()
                .mapToDouble(OrderItem::getSubTotal).sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
