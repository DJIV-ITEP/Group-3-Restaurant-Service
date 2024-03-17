package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.services.RestaurantService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
}
