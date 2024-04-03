package com.itep.restaurant_service.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itep.restaurant_service.models.OrderResource;
import com.itep.restaurant_service.services.OrderService;

@Component
public class OrderMessageListener {
	@Autowired
	private OrderService orderService;

	public void listenOrderMessage(OrderResource order) {

		/*
		 * first get order status If the order status is Paid , get customer delivery
		 * 
		 * 
		 */

		System.out.println("inside the listenier ............." + order.getCustomerName() + order.getTotal()
				+ order.getCustomerId() + order.getRestaurantName());

		// if (order.getOrderStatus() == OrderStatus.Paid)
		// order.setOrderStatus(OrderStatus.Finished);

		orderService.publishUpdateOrderStatus(order.OrderStatusID, order.getOrderStatus().Created);

	}

}