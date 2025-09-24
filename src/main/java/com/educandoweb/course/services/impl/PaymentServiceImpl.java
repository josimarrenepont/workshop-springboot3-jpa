package com.educandoweb.course.services.impl;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.Payment;
import com.educandoweb.course.repositories.OrderRepository;
import com.educandoweb.course.services.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {


    private final OrderRepository orderRepository;
    private final StockServiceImpl stockServiceImpl;

    public PaymentServiceImpl(OrderRepository orderRepository, StockServiceImpl stockServiceImpl) {
        this.orderRepository = orderRepository;
        this.stockServiceImpl = stockServiceImpl;
    }

    @Transactional
    public Payment processPayment(Order order) {
        if (order.getPayment() == null) {
            throw new IllegalArgumentException("Payment details are missing for the order.");
        }

        stockServiceImpl.validateStock(order.getItems());

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