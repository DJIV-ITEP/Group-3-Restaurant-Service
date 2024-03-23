package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
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
    public void testGetMenus() {
        List<MenuResource> menus = new ArrayList<>();
        menus.add(new MenuResource(1L, "Menu 1"));
        menus.add(new MenuResource(2L, "Menu 2"));

        Mockito.when(menuService.getAllMenues()).thenReturn(menus);

        List<MenuResource> result = menuController.getMenus();

        assertEquals(2, result.size());
        assertEquals("Menu 1", result.get(0).getName());
        assertEquals("Menu 2", result.get(1).getName());
    }

    @Test
    public void testCreateMenu() throws Exception {
        MenuEntity newMenu = new MenuEntity("New Menu");
        MenuResource newMenuResource = new MenuResource(1L, "New Menu");

        Mockito.when(menuService.createMenu(Mockito.any(MenuEntity.class))).thenReturn(newMenuResource);

        ResponseEntity<Object> response = menuController.createMenu(newMenu);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu created successfully", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    public void testUpdateMenu() throws Exception {
        long id = 1L;
        MenuEntity updatedMenu = new MenuEntity("Updated Menu");
        MenuResource updatedMenuResource = new MenuResource(id, "Updated Menu");

        Mockito.when(menuService.updateMenu(Mockito.eq(id), Mockito.any(MenuEntity.class))).thenReturn(updatedMenuResource);

        ResponseEntity<Object> response = menuController.updateMenu(id, updatedMenu);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu updated successfully", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    public void testDeleteMenu() throws Exception {
        long id = 1L;

        ResponseEntity<Object> response = menuController.deleteMenu(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu deleted successfully", ((Map<?, ?>) response.getBody()).get("message"));
    }



}
