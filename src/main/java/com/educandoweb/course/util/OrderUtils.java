package com.educandoweb.course.util;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.OrderItem;

public final class OrderUtils {

    private static final String NO_ITEMS = "The order must contain at least one time";
    private static final String NO_USER = "The order must be associated with a costumer";
    private static final String NO_PAYMENT = "The order must have a payment defined";

    private OrderUtils(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static double calculateTotal(Order order) {

        if(order.getItems() == null || order.getItems().isEmpty()){
            return 0.0;
        }

        double itemsTotal =  order.getItems().stream()
                .mapToDouble(OrderItem::getSubTotal)
                .sum();

        double discount = order.getDiscount();
        double shippingCost = order.getShippingCost();

        return itemsTotal + shippingCost - discount;
    }

    public static void validateOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException(NO_ITEMS);
        }
        if (order.getUser() == null) {
            throw new IllegalArgumentException(NO_USER);
        }
        if(order.getPayment() == null){
            throw new IllegalArgumentException(NO_PAYMENT);
        }
    }
}
