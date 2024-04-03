package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.MenuEntity;

import java.util.List;

public interface MenuService {
    List<MenuResource> getMenues(long rest_id,long cat_id) throws Exception;

    MenuResource createMenu(long rest_id,long cat_id, MenuEntity body) throws Exception;

    MenuResource updateMenu(long rest_id, long cat_id,long id, MenuEntity body) throws Exception;

    void deleteMenu(long rest_id, long cat_id,long id) throws Exception;
}
