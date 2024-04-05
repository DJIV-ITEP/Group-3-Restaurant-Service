package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.services.impl.MenuServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MenuController {

    private final MenuServiceImpl menuService;
    public MenuController(MenuServiceImpl menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/restaurants/{rest_id}/category/{cat_id}/menus")
    public ResponseEntity<Object> getMenus(@PathVariable long rest_id, @PathVariable long cat_id) throws Exception {

        List<MenuResource> result = menuService.getMenues(rest_id, cat_id);
        if(!result.isEmpty()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>( Map.of("message","no menu found for this category", "status",200)
                    , HttpStatus.OK);
        }
    }

    @GetMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}")
    public ResponseEntity<Object> getMenuDetails(@PathVariable long rest_id, @PathVariable long cat_id, @PathVariable long menu_id) throws Exception {
        var menuResource = menuService.getMenueDetails(rest_id,cat_id,menu_id);
        return menuResource.<ResponseEntity<Object>>map(
                        resource -> new ResponseEntity<>(
                                resource, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        Map.of("message", "Menu not found",
                                "status", 404)
                        , HttpStatus.NOT_FOUND));

    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/restaurants/{rest_id}/category/{cat_id}/menus")
    public ResponseEntity<Object> createMenu(@PathVariable long rest_id, @PathVariable long cat_id ,@RequestBody MenuEntity addMenu) throws Exception {

            MenuResource addResource = menuService.createMenu(rest_id,cat_id, addMenu);
            return new ResponseEntity<>(Map.of(
                    "message","Menu created successfully",
                    "status",200
            ), HttpStatus.OK);



    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{id}")
    public ResponseEntity<Object> updateMenu(@PathVariable long rest_id,@PathVariable long cat_id,@PathVariable long id, @RequestBody MenuEntity updatedMenu) throws Exception {

            MenuResource updatedResource = menuService.updateMenu(rest_id,cat_id,id, updatedMenu);
            return ResponseEntity.ok(Map.of(
                    "message", "Menu updated successfully",
                    "status", 200
            ));

    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @DeleteMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{id}")
    public ResponseEntity<Object> deleteMenu(@PathVariable long rest_id, @PathVariable long cat_id,@PathVariable long id) throws Exception {

            menuService.deleteMenu(rest_id,cat_id,id);
            return ResponseEntity.ok(Map.of(
                    "message", "Menu deleted successfully",
                    "status", 200
            ));

    }
}
