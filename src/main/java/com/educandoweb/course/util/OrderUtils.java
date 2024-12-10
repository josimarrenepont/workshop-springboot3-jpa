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
            throw new IllegalArgumentException("The order must contain at least one item.");
        }
        if (order.getClient() == null) {
            throw new IllegalArgumentException("The order must be associated with a customer.");
        }
    }
}
