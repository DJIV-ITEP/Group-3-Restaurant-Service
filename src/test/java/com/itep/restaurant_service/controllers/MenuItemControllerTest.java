package com.itep.restaurant_service.controllers;


import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.impl.MenuItemServiceImpl;
import org.junit.Assert;
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


@RunWith(MockitoJUnitRunner.class)
public class MenuItemControllerTest {
    @Mock
    private MenuItemServiceImpl menuItemService;

    @InjectMocks
    private MenuItemController menuItemController;

    @Test
    public void testGetItems() throws Exception {
        List<ItemResource> items = new ArrayList<>();
        RestaurantEntity res = new RestaurantEntity();
        CategoryEntity cat = new CategoryEntity(1L,"cat1",res, null);
        MenuEntity men = new MenuEntity(1L,"menu 1", cat,null);
        items.add(new ItemResource(1L , "item 1",1200,"the descrip of item", 1L));

        // Mock service method
        Mockito.when(menuItemService.getItems(1L,1L,1L)).thenReturn(items);

        // Call controller method
        List<ItemResource> result = menuItemController.getItems(1L,1L,1L);

        // Assert
        Assert.assertEquals(items, result);


    }

    @Test
    public void testCreateItem() throws Exception{
        // Mock data
        RestaurantEntity res = new RestaurantEntity();
        CategoryEntity cat = new CategoryEntity(1L,"cat1",res, null);
        MenuEntity men = new MenuEntity(1L,"menu 1", cat,null);
        ItemEntity addItem = new ItemEntity(1L, "item 1", "description",1200, men);
        ItemResource addResource = new ItemResource(1L, "item 2",1200,"descript",1L );

        // Mock service method
        Mockito.when(menuItemService.createItem(1L,1L,1L,addItem)).thenReturn(addResource);

        // Call controller method
        ResponseEntity<Object> responseEntity = menuItemController.createItem(1L,1L,1L,addItem);

        // Assert
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testUpdateItem() throws Exception{
        // Mock data
        Long id = 1L;
        Long rest_id = 1L;
        Long cat_id = 1L;
        Long menu_id = 1L;
        ItemEntity updatedItem = new ItemEntity();
        ItemResource updatedResource = new ItemResource();

        // Mock service method
        Mockito.when(menuItemService.updateItem(rest_id,cat_id,menu_id,id, updatedItem)).thenReturn(updatedResource);

        // Call controller method
        ResponseEntity<Object> responseEntity = menuItemController.updateItem(rest_id,cat_id,menu_id,id, updatedItem);

        // Assert
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testDeleteItem() {
        // Mock data
        Long rest_id = 1L;
        Long cat_id = 1L;
        Long menu_id = 1L;
        Long id = 1L;

        // Call controller method
        ResponseEntity<Object> responseEntity = menuItemController.deleteItem(rest_id,cat_id,menu_id,id);

        // Assert
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

}
