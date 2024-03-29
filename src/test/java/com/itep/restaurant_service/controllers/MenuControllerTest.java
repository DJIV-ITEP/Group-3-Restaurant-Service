package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.impl.MenuServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class MenuControllerTest {
    @Mock
    private MenuServiceImpl menuService;

    @InjectMocks
    private MenuController menuController;

    @Test
    public void testGetMenus() throws Exception{
        List<MenuResource> menus = new ArrayList<>();
        RestaurantEntity res = new RestaurantEntity();
        CategoryEntity cat = new CategoryEntity(1L,"cat1",res);
        menus.add(new MenuResource(1L, "Menu 1",cat.getId()));
        menus.add(new MenuResource(2L, "Menu 2",cat.getId()));

        Mockito.when(menuService.getMenues(1L,1L)).thenReturn(menus);

        List<MenuResource> result = menuController.getMenus(1L,1L);

        assertEquals(2, result.size());
        assertEquals("Menu 1", result.get(0).getName());
        assertEquals("Menu 2", result.get(1).getName());
    }

    @Test
    public void testCreateMenu() throws Exception {
        MenuEntity newMenu = new MenuEntity("New Menu");
        RestaurantEntity res = new RestaurantEntity();
        CategoryEntity cat = new CategoryEntity(1L,"cat1",res);
        MenuResource newMenuResource = new MenuResource(1L, "New Menu",cat.getId());

        Mockito.when(menuService.createMenu(1L,1L,Mockito.any(MenuEntity.class))).thenReturn(newMenuResource);

        ResponseEntity<Object> response = menuController.createMenu(1L,1L,newMenu);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu created successfully", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    public void testUpdateMenu() throws Exception {
        long id = 1L;
        long rest_id = 1L;
        long cat_id = 1L;
        MenuEntity updatedMenu = new MenuEntity("Updated Menu");
        RestaurantEntity res = new RestaurantEntity();
        CategoryEntity cat = new CategoryEntity(1L,"cat1",res);
        MenuResource updatedMenuResource = new MenuResource(id, "Updated Menu",cat.getId());

        Mockito.when(menuService.updateMenu(Mockito.eq(rest_id),Mockito.eq(cat_id),Mockito.eq(id), Mockito.any(MenuEntity.class))).thenReturn(updatedMenuResource);

        ResponseEntity<Object> response = menuController.updateMenu(rest_id,cat_id,id, updatedMenu);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu updated successfully", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    public void testDeleteMenu() throws Exception {
        long id = 1L;
        long rest_id = 1L;
        long cat_id = 1L;

        ResponseEntity<Object> response = menuController.deleteMenu(rest_id,cat_id,id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu deleted successfully", ((Map<?, ?>) response.getBody()).get("message"));
    }



}
