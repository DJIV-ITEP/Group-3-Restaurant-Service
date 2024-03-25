package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;

import java.util.List;

public interface MenuItemService {
    List<ItemResource> getAllItems(Long menu_id);

    ItemResource createItem(Long menu_id, ItemEntity body) throws Exception;

    ItemResource updateItem(Long id, ItemEntity body) throws Exception;

    void deleteItem(Long id) throws Exception;
}
