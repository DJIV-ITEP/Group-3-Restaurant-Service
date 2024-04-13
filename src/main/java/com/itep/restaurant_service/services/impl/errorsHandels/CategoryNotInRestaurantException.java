package com.itep.restaurant_service.services.impl.errorsHandels;

public class CategoryNotInRestaurantException extends RuntimeException{
    public CategoryNotInRestaurantException(long categoryId) {
        super("Category with id " + categoryId + " not in Restaurant");
    }
}
