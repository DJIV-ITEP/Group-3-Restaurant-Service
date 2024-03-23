package com.itep.restaurant_service.controllers;


import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
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
    public void testGetItems() {
        List<ItemResource> items = new ArrayList<>();
        items.add(new ItemResource(/* Item details here */));

        // Mock service method
        Mockito.when(menuItemService.getAllItems()).thenReturn(items);

        // Call controller method
        List<ItemResource> result = menuItemController.getItems();

        // Assert
        Assert.assertEquals(items, result);


    }

    @Test
    public void testCreateItem() throws Exception{
        // Mock data
        ItemEntity addItem = new ItemEntity(/* Item details here */);
        ItemResource addResource = new ItemResource(/* Item details here */);

        // Mock service method
        Mockito.when(menuItemService.createItem(addItem)).thenReturn(addResource);

        // Call controller method
        ResponseEntity<Object> responseEntity = menuItemController.createItem(addItem);

        // Assert
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testUpdateItem() throws Exception{
        // Mock data
        Long id = 1L;
        ItemEntity updatedItem = new ItemEntity(/* Updated item details here */);
        ItemResource updatedResource = new ItemResource(/* Updated item details here */);

        // Mock service method
        Mockito.when(menuItemService.updateItem(id, updatedItem)).thenReturn(updatedResource);

        // Call controller method
        ResponseEntity<Object> responseEntity = menuItemController.updateItem(id, updatedItem);

        // Assert
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testDeleteItem() {
        // Mock data
        Long id = 1L;

        // Call controller method
        ResponseEntity<Object> responseEntity = menuItemController.deleteItem(id);

        // Assert
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

}
