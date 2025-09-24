package com.educandoweb.course.services.impl;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.educandoweb.course.repositories.OrderRepository;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.services.OrderService;
import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import com.educandoweb.course.util.OrderUtils;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository repository;
	private final StockServiceImpl stockServiceImpl;
	private final PaymentServiceImpl paymentServiceImpl;
	private final ProductRepository productRepository;

	public OrderServiceImpl(OrderRepository repository, StockServiceImpl stockServiceImpl, PaymentServiceImpl paymentServiceImpl,
							ProductRepository productRepository){
		this.repository = repository;
		this.stockServiceImpl = stockServiceImpl;
		this.paymentServiceImpl = paymentServiceImpl;
		this.productRepository = productRepository;
	}
	@Override
	public List<Order> findAll() {
		return repository.findAll();
	}
	@Override
	public Order findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	@Override
	@Transactional
	public Order create(Order order) {

		order.setMoment(Instant.now());
		order.setOrderStatus(OrderStatus.PENDING);

		stockServiceImpl.validateStock(order.getItems());
		OrderUtils.validateOrder(order);

		Double total = OrderUtils.calculateTotal(order);
		order.setTotal(total);

		Order savedOrder = repository.save(order);

		stockServiceImpl.updateStock(order.getItems());

		return savedOrder;
	}
	@Override
	@Transactional
	public Order processpayment(Long orderId){
		Order order = findById(orderId);
		if(order.getOrderStatus() != OrderStatus.PENDING){
			throw new IllegalArgumentException("Payment has already been processed or order cancelled.");
		}
		paymentServiceImpl.processPayment(order);
		order.setOrderStatus(OrderStatus.PAID);
		stockServiceImpl.updateStock(order.getItems());
		return repository.save(order);
	}
	@Override
	@Transactional
	public Order update(Long id, Order updatedOrder) {
		Order existingOrder = findById(id);
		existingOrder.setUser(updatedOrder.getUser());
		existingOrder.setMoment(updatedOrder.getMoment());
		existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
		existingOrder.setPayment(updatedOrder.getPayment());
		return repository.save(existingOrder);
	}
	@Override
    public void delete(Long id) {
		try{
			repository.deleteById(id);
		} catch(EmptyResultDataAccessException e){
			throw new ResourceNotFoundException("Order not found for id: " + id);
		} catch (DataIntegrityViolationException e){
			throw new DatabaseException("Integrity violation - " + e.getMessage());
		}
    }
}
