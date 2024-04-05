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
    public ResponseEntity<Object> getMenus(@PathVariable Long rest_id, @PathVariable Long cat_id) throws Exception {

        List<MenuResource> result = menuService.getMenues(rest_id, cat_id);
        if(!result.isEmpty()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>( Map.of("message","no menu found for this category", "status",200)
                    , HttpStatus.OK);
        }
    }


    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/restaurants/{rest_id}/category/{cat_id}/menus")
    public ResponseEntity<Object> createMenu(@PathVariable Long rest_id, @PathVariable Long cat_id ,@RequestBody MenuEntity addMenu) throws Exception {

            MenuResource addResource = menuService.createMenu(rest_id,cat_id, addMenu);
            return new ResponseEntity<>(Map.of(
                    "message","Menu created successfully",
                    "status",200
            ), HttpStatus.OK);



    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{id}")
    public ResponseEntity<Object> updateMenu(@PathVariable Long rest_id,@PathVariable Long cat_id,@PathVariable Long id, @RequestBody MenuEntity updatedMenu) throws Exception {

            MenuResource updatedResource = menuService.updateMenu(rest_id,cat_id,id, updatedMenu);
            return ResponseEntity.ok(Map.of(
                    "message", "Menu updated successfully",
                    "status", 200
            ));

    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @DeleteMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{id}")
    public ResponseEntity<Object> deleteMenu(@PathVariable Long rest_id, @PathVariable Long cat_id,@PathVariable Long id) throws Exception {

            menuService.deleteMenu(rest_id,cat_id,id);
            return ResponseEntity.ok(Map.of(
                    "message", "Menu deleted successfully",
                    "status", 200
            ));

    }
}
