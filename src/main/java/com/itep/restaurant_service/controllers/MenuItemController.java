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
    public ResponseEntity<Object> getItems(@PathVariable Long rest_id,@PathVariable Long cat_id,@PathVariable Long menu_id) throws Exception {
        List<ItemResource> result = menuItemService.getItems(rest_id,cat_id,menu_id);
        return new ResponseEntity<>(Map.of(
                "code",200,
                "data",Map.of("items",result)
        ), HttpStatus.OK);


    }
    @GetMapping("/restaurants/item")
    public ResponseEntity<Object> getItems(@RequestBody IntArrayReques request) throws Exception {
        Integer[] itemsIds = request.getItemsIds();
        List<ItemResource> result = menuItemService.getItemsbyIds(itemsIds);
        return new ResponseEntity<>(
                Map.of(
                        "code",200,
                        "data",Map.of("items",result)
                        ), HttpStatus.OK);
    }

    @GetMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/{item_id}")
    public ResponseEntity<Object> getItemDetails(@PathVariable long rest_id, @PathVariable long cat_id, @PathVariable long menu_id, @PathVariable long item_id) throws Exception {
        var itemResource = menuItemService.getItemsDetails(rest_id,cat_id,menu_id,item_id);
        return itemResource.<ResponseEntity<Object>>map(
                        resource -> new ResponseEntity<>(
                                resource, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        Map.of("message", "item not found",
                                "status", 404)
                        , HttpStatus.NOT_FOUND));

    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item")
    public ResponseEntity<Object> createItem(@PathVariable Long rest_id,@PathVariable Long cat_id,@PathVariable Long menu_id,@RequestBody ItemEntity addItem) throws Exception {

            ItemResource addResource = menuItemService.createItem(rest_id,cat_id,menu_id, addItem);
            return ResponseEntity.ok(Map.of(
                    "message","Menu Item created successfully",
                    "status",200

            ));


    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/{id}")
    public  ResponseEntity<Object> updateItem(@PathVariable Long rest_id,@PathVariable Long cat_id,@PathVariable Long menu_id,@PathVariable Long id, @RequestBody ItemEntity updatedItem) throws Exception {

            ItemResource updatedResource = menuItemService.updateItem(rest_id,cat_id,menu_id,id, updatedItem);
            return ResponseEntity.ok(Map.of(
                    "message", "Menu Item updated successfully",
                    "status", 200
            ));

    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @DeleteMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long rest_id,@PathVariable Long cat_id,@PathVariable Long menu_id,@PathVariable Long id) throws Exception {

            menuItemService.deleteItem(rest_id,cat_id,menu_id,id);
            return ResponseEntity.ok(Map.of(
                    "message", "Menu deleted successfully",
                    "status", 200
            ));

    }
}
