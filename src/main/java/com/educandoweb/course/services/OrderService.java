package com.educandoweb.course.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.Product;
import com.educandoweb.course.repositories.OrderItemRepository;
import com.educandoweb.course.repositories.ProductRepository;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private ProductRepository productRepository;

	public List<Order> findAll(){
		return repository.findAll();
	}
	
	public Order findById(Long id) {
		Optional<Order> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public Order processOrder(Order order){
		for(OrderItem item : order.getItems()){
			Product product = item.getProduct();

			if(product.getQunatityInStock() < item.getQuantity()){
				throw new IllegalArgumentException(
						"Insufficient stock for the product: " + product.getName() + item.getQuantity());
			}
			product.setQunatityInStock(product.getQunatityInStock() - item.getQuantity());
		}
		Order savedOrder = repository.save(order);
		productRepository.saveAll(order.getItems().stream()
				.map(OrderItem::getProduct).toList());

		return savedOrder;
	}

	public Order create(Order order) {
		return repository.save(order);
	}

	public Order altered(Long id, Order obj) {
		Optional<Order> existingOrder = repository.findById(id);
		if(existingOrder.isEmpty()){
			throw new ResourceNotFoundException("Request not found for ID: " + id);
		}
		Order orderUpdate = existingOrder.get();
		orderUpdate.setClient(obj.getClient());
		orderUpdate.setMoment(obj.getMoment());
		orderUpdate.setOrderStatus(obj.getOrderStatus());
		orderUpdate.setPayment(obj.getPayment());

		return repository.save(orderUpdate);
	}
}
