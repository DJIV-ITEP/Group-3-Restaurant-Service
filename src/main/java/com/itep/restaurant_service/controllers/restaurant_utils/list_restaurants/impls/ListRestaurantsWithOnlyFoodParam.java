package com.itep.restaurant_service.controllers.restaurant_utils.list_restaurants.impls;

import com.itep.restaurant_service.controllers.restaurant_utils.list_restaurants.ListRestaurantsHandler;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ListRestaurantsWithOnlyFoodParam implements ListRestaurantsHandler {
    private final ListRestaurantsHandler successor;

    public ListRestaurantsWithOnlyFoodParam(ListRestaurantsHandler successor) {
        this.successor = successor;
    }

    @Override
    public ResponseEntity<Object> handleRequest(Map<String, Object> filtersMap, RestaurantServiceImpl restaurantService) {
        if (filtersMap.get("food")!=null&&!filtersMap.get("food").toString().isEmpty()) {
            return new ResponseEntity<>(
                    restaurantService.getAvailableFilteredRestaurantsByFood(filtersMap.get("food").toString())
                    , HttpStatus.OK);
        } else if (successor != null) {
            return successor.handleRequest(filtersMap, restaurantService);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

