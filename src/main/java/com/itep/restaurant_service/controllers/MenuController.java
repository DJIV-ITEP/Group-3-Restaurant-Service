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
    public List<MenuResource> getMenus(@PathVariable Long rest_id, @PathVariable Long cat_id) throws Exception {
//        try{
//            List<MenuResource> getResource = menuService.getMenues(cat_id);
//            return ResponseEntity.ok(Map.of(
//                    "message","Menu created successfully",
//                    "status",200,
//                    "data",addResource
//            ));
//
//        }
//        catch (Exception e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                    Map.of("message",e.getMessage(),
//                            "status", 400)
//            );
//        }

        return menuService.getMenues(rest_id, cat_id);
    }

//    @GetMapping("/menus")
//    public ResponseEntity<Object> getMenus(){
//        try {
//            List<MenuResource> listResource = menuService.getAllMenues();
//            return ResponseEntity.ok(Map.of(
//                    "message","Menu created successfully",
//                    "status",200,
//                    "data",listResource
//            ));
//
//        }
//        catch (Exception e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                    Map.of("message",e.getMessage(),
//                            "status", 400)
//            );
//
//        }
//    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/restaurants/{rest_id}/category/{cat_id}/menus")
    public ResponseEntity<Object> createMenu(@PathVariable Long rest_id, @PathVariable Long cat_id ,@RequestBody MenuEntity addMenu){
        try {
            MenuResource addResource = menuService.createMenu(rest_id,cat_id, addMenu);
            return ResponseEntity.ok(Map.of(
                    "message","Menu created successfully",
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
    @PutMapping("/restaurants/{rest_id}/category/{cat_id}/menus/update/{id}")
    public ResponseEntity<Object> updateMenu(@PathVariable Long rest_id,@PathVariable Long cat_id,@PathVariable Long id, @RequestBody MenuEntity updatedMenu) {
        try {
            MenuResource updatedResource = menuService.updateMenu(rest_id,cat_id,id, updatedMenu);
            return ResponseEntity.ok(Map.of(
                    "message", "Menu updated successfully",
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
    @DeleteMapping("/restaurants/{rest_id}/category/{cat_id}/menus/delete/{id}")
    public ResponseEntity<Object> deleteMenu(@PathVariable Long rest_id, @PathVariable Long cat_id,@PathVariable Long id) {
        try {
            menuService.deleteMenu(rest_id,cat_id,id);
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
