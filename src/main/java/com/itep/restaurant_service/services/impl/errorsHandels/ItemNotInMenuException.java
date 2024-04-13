package com.itep.restaurant_service.services.impl.errorsHandels;

public class ItemNotInMenuException extends RuntimeException{
    public ItemNotInMenuException(long itemId) {
        super("Item with id " + itemId + " not in Menu");
    }
}
