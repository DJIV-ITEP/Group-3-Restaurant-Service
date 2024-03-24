package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    List<RestaurantResource> getAvailableRestaurants();
    List<RestaurantResource> getAvailableFilteredRestaurants(String food, String cuisine);
    RestaurantResource createRestaurant(RestaurantEntity body) throws Exception;

    Optional<RestaurantResource> getRestaurantDetails(long restaurantId);
    Optional<RestaurantResource> getRestaurantByUsername(String username);
    void setRestaurantStatus(long id, String status);
}
