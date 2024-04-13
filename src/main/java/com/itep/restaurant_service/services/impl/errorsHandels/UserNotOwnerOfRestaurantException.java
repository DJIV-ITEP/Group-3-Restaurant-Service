package com.itep.restaurant_service.services.impl.errorsHandels;

public class UserNotOwnerOfRestaurantException extends RuntimeException{
    public UserNotOwnerOfRestaurantException() {
        super("You are not the Owner of Restaurant");
    }
}
