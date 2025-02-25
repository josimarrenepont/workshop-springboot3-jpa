package com.educandoweb.course.entities.dto;

public class OrderItemDTO {

    private Long id;
    private Integer quantity;
    private Double price;
    private Long orderId;

    public OrderItemDTO(){
    }

    public OrderItemDTO(Long id, Integer quantity, Double price, Long orderId) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
