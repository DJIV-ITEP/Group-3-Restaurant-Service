package com.itep.restaurant_service.controllers.restaurant_utils.list_restaurants;

import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.springframework.http.ResponseEntity;

import java.util.Map;

//Chain of Responsible Design Pattern
public interface ListRestaurantsHandler {
    ResponseEntity<Object> handleRequest(String food, String cuisine, RestaurantServiceImpl restaurantService);
}
