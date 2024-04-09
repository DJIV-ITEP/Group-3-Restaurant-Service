package com.itep.restaurant_service.services.impl.errorsHandels;

public class MenuNotFoundException extends RuntimeException{
    public MenuNotFoundException(long menuId) {
        super("Menu with id " + menuId + " not found");
    }
}

