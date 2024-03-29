package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.models.UserResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    List<RestaurantResource> getAvailableRestaurants();
    List<RestaurantResource> getAvailableFilteredRestaurantsByFoodAndCuisine(String food, String cuisine);
    List<RestaurantResource> getAvailableFilteredRestaurantsByFood(String food);
    List<RestaurantResource> getAvailableFilteredRestaurantsByCuisine(String cuisine);
    RestaurantResource createRestaurant(RestaurantEntity body) throws Exception;

    Optional<RestaurantResource> getRestaurantDetails(long restaurantId);
    Optional<UserEntity> getRestaurantOwner(long restaurantId);
    UserDetails getRestaurantUserByUsername(String username);

    void setRestaurantStatus(long id, String status);
}
