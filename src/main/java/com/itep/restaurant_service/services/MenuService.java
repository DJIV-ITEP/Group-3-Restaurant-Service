package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.MenuEntity;

import java.util.List;

public interface MenuService {
    List<MenuResource> getAllMenues();

    MenuResource createMenu(MenuEntity body) throws Exception;

    MenuResource updateMenu(Long id, MenuEntity body) throws Exception;

    void deleteMenu(Long id) throws Exception;
}
