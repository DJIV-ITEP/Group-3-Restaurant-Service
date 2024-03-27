package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.MenuEntity;

import java.util.List;

public interface MenuService {
    List<MenuResource> getMenues(Long cat_id);

    MenuResource createMenu(Long rest_id,Long cat_id, MenuEntity body) throws Exception;

    MenuResource updateMenu(Long rest_id, Long cat_id,Long id, MenuEntity body) throws Exception;

    void deleteMenu(Long rest_id, Long cat_id,Long id) throws Exception;
}
