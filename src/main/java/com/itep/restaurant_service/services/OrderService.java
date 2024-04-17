package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.OrderResource;
import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    public  ResponseEntity<String> markOrderAsReady(long restaurantId, String orderId) throws Exception;

    public List<OrderResource> getIncomingOrders(long restaurantId) throws Exception;

    public ResponseEntity<String> cancelOrder(long restaurantId, String orderId) throws Exception;
    public ResponseEntity<String> accaptOrder(long restaurantId, String orderId) throws Exception;
//    OrderResource getOrderByRestaurantId(long restaurantId) throws Exception;


}
