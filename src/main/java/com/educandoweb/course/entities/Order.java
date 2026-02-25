package com.educandoweb.course.entities;

import com.educandoweb.course.entities.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "tb_order")
public class Order implements Serializable {

	private static final String NO_ITEMS = "The order must contain at least one time";
	private static final String NO_USER = "The order must be associated with a costumer";
	private static final String NO_PAYMENT = "The order must have a payment defined";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant moment;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status")
	private OrderStatus orderStatus;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "id.order")
	private Set<OrderItem> items = new HashSet<>();

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private Payment payment;

	private Double discount;
	private Double shippingCost;

	public Order() {}

	public Order(Long id, Instant moment, OrderStatus orderStatus, User user) {
		this.id = id;
		this.moment = moment;
		setOrderStatus(orderStatus);
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Double getTotal() {
		return calculateTotal();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<OrderItem> getItems() {
		return items;
	}

	public void setItems(Set<OrderItem> items) {
		this.items = items;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Double getDiscount() {
		return discount == null ? 0.0 : discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getShippingCost() {
		return shippingCost == null ? 0.0 : shippingCost;
	}

	public void setShippingCost(Double shippingCost) {
		this.shippingCost = shippingCost;
	}

	public double calculateTotal() {
		return items.stream()
				.mapToDouble(OrderItem::getSubTotal)
				.sum() + getShippingCost() - getDiscount();
	}

	public void validate() {
		if (items == null || items.isEmpty()) {
			throw new IllegalArgumentException(NO_ITEMS);
		}
		if (user == null) {
			throw new IllegalArgumentException(NO_USER);
		}
		if(payment == null){
			throw new IllegalArgumentException(NO_PAYMENT);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Order other = (Order) obj;
		return Objects.equals(id, other.id);
	}
}
