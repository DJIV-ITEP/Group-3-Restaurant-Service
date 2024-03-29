package com.itep.restaurant_service.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;

@RestController
public class RestaurantController {
    private final RestaurantServiceImpl restaurantService;

    public RestaurantController(RestaurantServiceImpl restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public List<RestaurantResource> getAvailableRestaurants() {
        return restaurantService.getAvailableRestaurants();
    }

    @GetMapping("/all_restaurants")
    public List<RestaurantResource> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/restaurants")
    public ResponseEntity<Object> createRestaurant(@RequestBody RestaurantEntity body) {
        try {
            restaurantService.createRestaurant(body);
            return new ResponseEntity<>(
                    Map.of("message", "Restaurant created successfully",
                            "status", 200),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("message", e.getMessage(),
                            "status", 400),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<Object> getRestaurantDetails(@PathVariable("restaurantId") long restaurantId) {
        var restaurantResource = restaurantService.getRestaurantDetails(restaurantId);
        return restaurantResource.<ResponseEntity<Object>>map(
                resource -> new ResponseEntity<>(
                        resource, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        Map.of("message", "Restaurant not found",
                                "status", 404),
                        HttpStatus.NOT_FOUND));

    }

}
