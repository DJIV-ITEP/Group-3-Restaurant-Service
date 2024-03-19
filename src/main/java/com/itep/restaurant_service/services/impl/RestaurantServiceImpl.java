package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.services.RestaurantService;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

}
