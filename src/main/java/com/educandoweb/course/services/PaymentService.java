package com.educandoweb.course.services;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.Payment;
import com.educandoweb.course.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StockService stockService;

    @Transactional
    public Payment processPayment(Order order) {
        if (order.getPayment() == null) {
            throw new IllegalArgumentException("Payment details are missing for the order.");
        }

        stockService.validateStock(order.getItems());

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