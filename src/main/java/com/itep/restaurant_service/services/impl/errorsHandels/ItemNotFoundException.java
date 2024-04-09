package com.itep.restaurant_service.services.impl.errorsHandels;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(long itemId) {
        super("Item with id " + itemId + " not found");
    }
}

