package com.itep.restaurant_service.services;

import java.util.List;
import java.util.Optional;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;

public interface RestaurantService {
    List<RestaurantResource> getAvailableRestaurants();

    List<RestaurantResource> getAllRestaurants();

    RestaurantResource createRestaurant(RestaurantEntity body) throws Exception;

    Optional<RestaurantResource> getRestaurantDetails(long restaurantId);
}
