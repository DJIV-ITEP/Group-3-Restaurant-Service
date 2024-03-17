package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<RestaurantResource> getAvailableRestaurants() {
//        TODO get only the ones which their status is online
        return restaurantRepository.findAll() .stream()
                .map(RestaurantEntity::toRestaurantResource)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResource createRestaurant(RestaurantEntity body) {
        return restaurantRepository.save(body).toRestaurantResource();
    }
}
