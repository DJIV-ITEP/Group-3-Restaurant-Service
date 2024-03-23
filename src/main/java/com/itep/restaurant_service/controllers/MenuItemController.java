package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.services.MenuItemService;
import com.itep.restaurant_service.services.impl.MenuItemServiceImpl;
import com.itep.restaurant_service.services.impl.MenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MenuItemController {

    private final MenuItemServiceImpl menuItemService;
    public MenuItemController(MenuItemServiceImpl menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item")
    public List<ItemResource> getItems(){
        return menuItemService.getAllItems();
    }
    @PostMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/create")
    public ResponseEntity<Object> createItem(@RequestBody ItemEntity addItem){
        try{
            ItemResource addResource = menuItemService.createItem(addItem);
            return ResponseEntity.ok(Map.of(
                    "message","Menu Item created successfully",
                    "status",200,
                    "data",addResource
            ));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("message",e.getMessage(),
                            "status", 400)
            );
        }
    }

    @PutMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/update/{id}")
    public  ResponseEntity<Object> updateItem(@PathVariable Long id, @RequestBody ItemEntity updatedItem){
        try {
            ItemResource updatedResource = menuItemService.updateItem(id, updatedItem);
            return ResponseEntity.ok(Map.of(
                    "message", "Menu Item updated successfully",
                    "status", 200,
                    "data", updatedResource
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("message", e.getMessage(), "status", 400)
            );
        }
    }

    @DeleteMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/delete/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long id) {
        try {
            menuItemService.deleteItem(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Menu deleted successfully",
                    "status", 200
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("message", e.getMessage(), "status", 400)
            );
        }
    }
}
