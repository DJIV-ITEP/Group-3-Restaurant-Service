package com.itep.restaurant_service.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;

import com.itep.restaurant_service.models.OrderResource;
import com.itep.restaurant_service.models.OrderResource.OrderStatus;

public interface OrderService {

    public String markOrderAsReady(@PathVariable Long restaurantId, @PathVariable Long orderId);

    public List<OrderResource> getIncomingOrders(@PathVariable Long restaurantId);

    public String cancelOrder(@PathVariable Long orderId);

    Optional<OrderResource> getOrderByRestaurantId(long restaurantId);

    public void publishUpdateOrderStatus(long IdOrder, OrderStatus NameStatus);

}
