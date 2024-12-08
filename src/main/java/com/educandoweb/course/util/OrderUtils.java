package com.educandoweb.course.util;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.OrderItem;

public class OrderUtils {

    public static Double calculateTotal(Order order) {
        return order.getItems().stream()
                .mapToDouble(OrderItem::getSubTotal)
                .sum();
    }

    public static void validateOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter pelo menos um item.");
        }
        if (order.getClient() == null) {
            throw new IllegalArgumentException("O pedido deve estar associado a um cliente.");
        }
    }
}
