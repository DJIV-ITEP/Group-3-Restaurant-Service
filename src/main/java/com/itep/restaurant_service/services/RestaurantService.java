package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface RestaurantService {
    List<RestaurantResource> getAvailableRestaurants();
    RestaurantResource createRestaurant(@RequestBody RestaurantEntity body);
}
