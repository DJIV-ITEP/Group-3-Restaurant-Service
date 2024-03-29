package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;

import java.util.List;

public interface MenuItemService {
    List<ItemResource> getItems(Long rest_id, Long cat_id,Long menu_id) throws Exception;

    ItemResource createItem(Long rest_id,Long cat_id,Long menu_id, ItemEntity body) throws Exception;

    ItemResource updateItem(Long rest_id,Long cat_id,Long menu_id,Long id, ItemEntity body) throws Exception;

    void deleteItem(Long rest_id,Long cat_id,Long menu_id,Long id) throws Exception;
}
