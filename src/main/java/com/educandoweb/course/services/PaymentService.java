package com.educandoweb.course.services;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.Payment;

public interface PaymentService {

    Payment processPayment(Order order);
}
