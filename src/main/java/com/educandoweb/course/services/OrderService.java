package com.educandoweb.course.services;


import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.Product;
import com.educandoweb.course.repositories.OrderRepository;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import com.educandoweb.course.util.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
	@Autowired
	private OrderRepository repository;

	@Autowired
	private StockService stockService;

	public List<Order> findAll() {
		return repository.findAll();
	}

	public Order findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public Order create(Order order) {
		// Validações
		OrderUtils.validateOrder(order);

		// Processar estoque
		stockService.validateStock(order.getItems());
		stockService.updateStock(order.getItems());

		// Processar pagamento
		paymentService.processPayment(order);

		// Calcular total do pedido
		Double total = OrderUtils.calculateTotal(order);
		System.out.println("Total do pedido: " + total);

		return repository.save(order);
	}

	public Order update(Long id, Order updatedOrder) {
		Order existingOrder = findById(id);
		existingOrder.setClient(updatedOrder.getClient());
		existingOrder.setMoment(updatedOrder.getMoment());
		existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
		existingOrder.setPayment(updatedOrder.getPayment());
		return repository.save(existingOrder);
	}

	@Autowired
	private PaymentService paymentService;

	public Order processOrder(Order order) {
		// Processar itens e estoque, conforme lógica existente
		for (OrderItem item : order.getItems()) {
			Product product = item.getProduct();
			if (product.getQuantityInStock() < item.getQuantity()) {
				throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
			}
			product.setQuantityInStock(product.getQuantityInStock() - item.getQuantity());
		}
		repository.save(order);

		// Processar o pagamento usando PaymentService
		paymentService.processPayment(order);

		return order;
	}

}
