package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.services.impl.MenuServiceImpl;
import com.itep.restaurant_service.services.impl.errorsHandels.*;
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
        try{
            List<MenuResource> result = menuService.getMenues(rest_id, cat_id);
            return new ResponseEntity<>(Map.of("code",200,
                    "data",Map.of("items",result)), HttpStatus.OK);
        }
        catch (RestaurantNotFoundException | CategoryNotFoundException e){
            return new ResponseEntity<>( Map.of("message",e.getMessage(), "status",404)
                    , HttpStatus.NOT_FOUND);
        }
        catch (CategoryNotInRestaurantException ee){
            return new ResponseEntity<>( Map.of("message",ee.getMessage(), "status",400)
                    , HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}")
    public ResponseEntity<Object> getMenuDetails(@PathVariable long rest_id, @PathVariable long cat_id, @PathVariable long menu_id) throws Exception {
        try{
            MenuResource menuResource = menuService.getMenueDetails(rest_id,cat_id,menu_id);
            return new ResponseEntity<>(Map.of("code",200,
        "data",Map.of("item",menuResource)),HttpStatus.OK);
        }
        catch (RestaurantNotFoundException | CategoryNotFoundException | MenuNotFoundException e){
            return new ResponseEntity<>(
                    Map.of("message", e.getMessage(),
                            "status", 404)
                    , HttpStatus.NOT_FOUND);
        }

        catch (CategoryNotInRestaurantException | MenuNotInCategoryException ee ){
            return new ResponseEntity<>(
                    Map.of("message", ee.getMessage(),
                            "status", 400)
                    , HttpStatus.BAD_REQUEST);
        }


    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/restaurants/{rest_id}/category/{cat_id}/menus")
    public ResponseEntity<Object> createMenu(@PathVariable long rest_id, @PathVariable long cat_id ,@RequestBody MenuEntity addMenu) throws Exception {
            try{
                MenuResource addResource = menuService.createMenu(rest_id,cat_id, addMenu);
                return new ResponseEntity<>(Map.of(
                        "message","Menu created successfully",
                        "status",200
                ), HttpStatus.OK);
            }
            catch (RestaurantNotFoundException | CategoryNotFoundException e){
                return new ResponseEntity<>(Map.of(
                        "message",e.getMessage(),
                        "status",404
                ), HttpStatus.NOT_FOUND);
            }
            catch (CategoryNotInRestaurantException ee){
                return new ResponseEntity<>(
                        Map.of("message", ee.getMessage(),
                                "status", 400)
                        , HttpStatus.BAD_REQUEST);
            }
            catch (UserNotOwnerOfRestaurantException  e1){
                return new ResponseEntity<>(
                        Map.of("message",e1.getMessage(), "code",403)
                        , HttpStatus.FORBIDDEN
                );

            }
            catch (Exception ex){
                return new ResponseEntity<>(
                        Map.of("message",ex.getMessage(), "code",400)
                        , HttpStatus.BAD_REQUEST
                );
            }




    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{id}")
    public ResponseEntity<Object> updateMenu(@PathVariable long rest_id,@PathVariable long cat_id,@PathVariable long id, @RequestBody MenuEntity updatedMenu) throws Exception {
            try {
                MenuResource updatedResource = menuService.updateMenu(rest_id,cat_id,id, updatedMenu);
                return new  ResponseEntity<>(Map.of(
                        "message", "Menu updated successfully",
                        "status", 200
                ),HttpStatus.OK);
            }
            catch (RestaurantNotFoundException | CategoryNotFoundException | MenuNotFoundException e){
                return new ResponseEntity<>(
                        Map.of("message",e.getMessage(), "code",404)
                        , HttpStatus.NOT_FOUND
                );
            }
            catch (UserNotOwnerOfRestaurantException  e1){
                return new ResponseEntity<>(
                        Map.of("message",e1.getMessage(), "code",403)
                        , HttpStatus.FORBIDDEN
                );

            }
            catch (CategoryNotInRestaurantException | MenuNotInCategoryException ee){
                return new ResponseEntity<>(
                        Map.of("message", ee.getMessage(),
                                "status", 400)
                        , HttpStatus.BAD_REQUEST);
            }
            catch (Exception ex){
                return new ResponseEntity<>(
                        Map.of("message",ex.getMessage(), "code",400)
                        , HttpStatus.BAD_REQUEST
                );
            }


    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @DeleteMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{id}")
    public ResponseEntity<Object> deleteMenu(@PathVariable long rest_id, @PathVariable long cat_id,@PathVariable long id) throws Exception {
            try{
                menuService.deleteMenu(rest_id,cat_id,id);
                return ResponseEntity.ok(Map.of(
                        "message", "Menu deleted successfully",
                        "status", 200
                ));
            }
            catch (RestaurantNotFoundException | CategoryNotFoundException | MenuNotFoundException e){
                return new ResponseEntity<>(
                        Map.of("message",e.getMessage(), "code",404)
                        , HttpStatus.NOT_FOUND
                );
            }
            catch (UserNotOwnerOfRestaurantException  e1){
                return new ResponseEntity<>(
                        Map.of("message",e1.getMessage(), "code",403)
                        , HttpStatus.FORBIDDEN
                );

            }
            catch (CategoryNotInRestaurantException | MenuNotInCategoryException ee){
                return new ResponseEntity<>(
                        Map.of("message", ee.getMessage(),
                                "status", 400)
                        , HttpStatus.BAD_REQUEST);
            }
            catch (Exception ex){
                return new ResponseEntity<>(
                        Map.of("message",ex.getMessage(), "code",400)
                        , HttpStatus.BAD_REQUEST
                );
            }

    }
}
