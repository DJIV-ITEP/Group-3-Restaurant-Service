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
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<ItemResource> getItems(@PathVariable Long menu_id){
        return menuItemService.getAllItems(menu_id);
    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item")
    public ResponseEntity<Object> createItem(@PathVariable Long menu_id,@RequestBody ItemEntity addItem){
        try{
            ItemResource addResource = menuItemService.createItem(menu_id, addItem);
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
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
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
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
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
