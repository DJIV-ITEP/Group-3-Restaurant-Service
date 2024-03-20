package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class RestaurantController {
    private final RestaurantServiceImpl restaurantService;

    public RestaurantController(RestaurantServiceImpl restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public List<RestaurantResource> getRestaurants(){
        return restaurantService.getAvailableRestaurants();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/restaurants")
    public ResponseEntity<Object> createRestaurant(@RequestBody RestaurantEntity body)  {
        try {
            restaurantService.createRestaurant(body);
            return new ResponseEntity<>(
                    Map.of("message","Restaurant created successfully",
                            "status", 200)
                    , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("message",e.getMessage(),
                            "status", 400)
                    , HttpStatus.OK);
        }
    }
}
