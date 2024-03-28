package com.itep.restaurant_service.controllers.restaurant_utils.list_restaurants.impls;

import com.itep.restaurant_service.controllers.restaurant_utils.list_restaurants.ListRestaurantsHandler;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ListRestaurantsWithOnlyCuisineParam implements ListRestaurantsHandler {
    private final ListRestaurantsHandler successor;

    public ListRestaurantsWithOnlyCuisineParam(ListRestaurantsHandler successor) {
        this.successor = successor;
    }

    @Override
    public ResponseEntity<Object> handleRequest(String food, String cuisine, RestaurantServiceImpl restaurantService) {
        if ((cuisine != null && !cuisine.isEmpty())) {
            return new ResponseEntity<>(
                    restaurantService.getAvailableFilteredRestaurantsByCuisine(cuisine)
                    , HttpStatus.OK);
        } else if (successor != null) {
            return successor.handleRequest(food, cuisine, restaurantService);
        }
        return new ResponseEntity<>(
                Map.of("message","could not list the restaurants", "status",400)
                , HttpStatus.BAD_REQUEST);
    }
}

