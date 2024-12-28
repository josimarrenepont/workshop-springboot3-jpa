package com.educandoweb.course.services;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.Payment;
import com.educandoweb.course.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    public Payment processPayment(Order order) {
        if (order.getPayment() == null) {
            throw new IllegalArgumentException("Payment details are missing for the order.");
        }

        Payment payment = order.getPayment();
        payment.setStatus("APPROVED");
        payment.setTransactionId(generateTransactionId());

        order.setPayment(payment);
        orderRepository.save(order);

        return payment;
    }
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }
}
