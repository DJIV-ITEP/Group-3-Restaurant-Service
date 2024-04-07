package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    List<ItemResource> getItems(long rest_id, long cat_id,long menu_id) throws Exception;
    Optional<ItemResource> getItemsDetails(long rest_id, long cat_id, long menu_id, long item_id) throws Exception;
    ItemResource createItem(long rest_id,long cat_id,long menu_id, ItemEntity body) throws Exception;

    ItemResource updateItem(long rest_id,long cat_id,long menu_id,long id, ItemEntity body) throws Exception;

    void deleteItem(long rest_id,long cat_id,long menu_id,long id) throws Exception;
}
