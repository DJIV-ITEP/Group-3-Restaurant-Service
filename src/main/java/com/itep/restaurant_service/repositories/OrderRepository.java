package com.itep.restaurant_service.repositories;

import java.util.List;

import com.itep.restaurant_service.models.OrderResource;

public interface OrderRepository {

    List<OrderResource> getAllOrders();

    OrderResource getOrderById(Long id);

    void saveOrder(OrderResource order);

    void deleteOrder(Long id);
}