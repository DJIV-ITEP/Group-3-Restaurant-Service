package com.itep.restaurant_service.services.impl.errorsHandels;

public class RestaurantNotFoundException extends RuntimeException{
    public RestaurantNotFoundException(long restaurantId) {
        super("Restaurant with id " + restaurantId + " not found");
    }
}
