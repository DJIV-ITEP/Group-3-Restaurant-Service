package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.models.UserResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    List<RestaurantResource> getAvailableRestaurants();
    List<RestaurantResource> getAvailableFilteredRestaurants(String food, String cuisine);
    RestaurantResource createRestaurant(RestaurantEntity body) throws Exception;

    Optional<RestaurantResource> getRestaurantDetails(long restaurantId);
    public Optional<UserEntity> getRestaurantOwner(long restaurantId);

        void setRestaurantStatus(long id, String status);
}
