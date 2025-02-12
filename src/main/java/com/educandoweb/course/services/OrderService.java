package com.educandoweb.course.services;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.Product;
import com.educandoweb.course.repositories.OrderRepository;
import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import com.educandoweb.course.util.OrderUtils;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

	private final OrderRepository repository;
	private final StockService stockService;
	private final PaymentService paymentService;

	public OrderService(OrderRepository repository, StockService stockService, PaymentService paymentService){
		this.repository = repository;
		this.stockService = stockService;
		this.paymentService = paymentService;
	}
	public List<Order> findAll() {
		return repository.findAll();
	}

	public Order findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	@Transactional
	public Order create(Order order) {

		OrderUtils.validateOrder(order);

		stockService.validateStock(order.getItems());

		double total = OrderUtils.calculateTotal(order);
		order.setTotal(total);

		Order savedOrder = repository.save(order);

		paymentService.processPayment(order);

		stockService.updateStock(order.getItems());

		return savedOrder;
	}

	@Transactional
	public Order update(Long id, Order updatedOrder) {
		Order existingOrder = findById(id);
		existingOrder.setUser(updatedOrder.getUser());
		existingOrder.setMoment(updatedOrder.getMoment());
		existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
		existingOrder.setPayment(updatedOrder.getPayment());
		return repository.save(existingOrder);
	}

	@Transactional
	public Order processOrder(Order order){

		stockService.validateStock(order.getItems());
			for(OrderItem item : order.getItems()){
				Product product = item.getProduct();
				if(product.getQuantityInStock() < item.getQuantity()){
					throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
				}
				product.setQuantityInStock(product.getQuantityInStock() - item.getQuantity());
			}
			stockService.updateStock(order.getItems());
			paymentService.processPayment(order);

			return repository.save(order);
	}

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
