package com.educandoweb.course.entities.enums;

public enum OrderStatus {

	PENDING(1),
	WAITING_PAYMENT(2),
	PAID(3),
	SHIPPED(4),
	DELIVERED(5),
	CANCELED(6);
	
	private final int code;
	
	private OrderStatus (int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static OrderStatus valueOf(int code) {
		for(OrderStatus value : OrderStatus.values()) {
			if(value.getCode() == code) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid OrderStatus code"); 
	}
}
