package com.itep.restaurant_service.services.impl.errorsHandels;

public class MenuNotInCategoryException extends RuntimeException{
    public MenuNotInCategoryException(long menuId) {
        super("Menu with id " + menuId + " not in Category");
    }
}
