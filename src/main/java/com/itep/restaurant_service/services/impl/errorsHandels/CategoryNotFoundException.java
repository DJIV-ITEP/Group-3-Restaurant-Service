package com.itep.restaurant_service.services.impl.errorsHandels;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(long categoryId) {
        super("Category with id " + categoryId + " not found");
    }
}

