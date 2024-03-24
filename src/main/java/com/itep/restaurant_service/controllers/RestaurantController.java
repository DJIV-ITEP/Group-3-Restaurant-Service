package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RestaurantController {
    private final RestaurantServiceImpl restaurantService;

    public RestaurantController(RestaurantServiceImpl restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public ResponseEntity<Object> getRestaurants(@RequestParam(value = "food", required = false) String food, @RequestParam(value = "cuisine", required = false) String cuisine){
        if (food == null && cuisine == null){
            return new ResponseEntity<>(
                    restaurantService.getAvailableRestaurants()
                    , HttpStatus.OK);
        }
        try {
            return new ResponseEntity<>(
                    restaurantService.getAvailableFilteredRestaurants(food, cuisine)
                    , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("message",e.getMessage(),
                            "status", 400)
                    , HttpStatus.BAD_REQUEST);

        }
    }

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
                    , HttpStatus.BAD_REQUEST);
        }
    }
}
