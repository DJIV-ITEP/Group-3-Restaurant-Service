package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.services.MenuItemService;
import com.itep.restaurant_service.services.impl.MenuItemServiceImpl;
import com.itep.restaurant_service.services.impl.MenuServiceImpl;
import com.itep.restaurant_service.services.impl.errorsHandels.*;
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
        try{
            List<ItemResource> result = menuItemService.getItems(rest_id,cat_id,menu_id);
            return new ResponseEntity<>(Map.of(
                    "code",200,
                    "data",Map.of("items",result)
            ), HttpStatus.OK);

        }
        catch (RestaurantNotFoundException | CategoryNotFoundException | MenuNotFoundException e){
            return new ResponseEntity<>(Map.of(
                    "code",404,
                    "status",e.getMessage()
            ), HttpStatus.NOT_FOUND);

        }
        catch (CategoryNotInRestaurantException | MenuNotInCategoryException ee){
            return new ResponseEntity<>(Map.of(
                    "status",400,
                    "message",ee.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            return new ResponseEntity<>(
                    Map.of(
                    "status",400,
                        "message",ex.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }



    }
    @PostMapping("/restaurants/item")
    public ResponseEntity<Object> getItems(@RequestBody Integer[] itemsIds) throws Exception {
        try{
//            Integer[] itemsIds = request.getItemsIds();
            List<ItemResource> result = menuItemService.getItemsbyIds(itemsIds);
            return new ResponseEntity<>(
                    Map.of(
                            "code",200,
                            "data",Map.of("items",result)
                    ), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    Map.of(
                            "status",400,
                            "message",e.getMessage()
                    ), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/{item_id}")
    public ResponseEntity<Object> getItemDetails(@PathVariable long rest_id, @PathVariable long cat_id, @PathVariable long menu_id, @PathVariable long item_id) throws Exception {
        try{
            ItemResource itemResource = menuItemService.getItemsDetails(rest_id,cat_id,menu_id,item_id);
            return new ResponseEntity<>(Map.of("code",200,
                    "data",Map.of("item",itemResource)),HttpStatus.OK);
        }
        catch (ItemNotFoundException | MenuNotFoundException | CategoryNotFoundException | RestaurantNotFoundException e){
            return new ResponseEntity<>(Map.of(
                    "code",404,
                    "status",e.getMessage()
            ), HttpStatus.NOT_FOUND);
        }

        catch (ItemNotInMenuException | MenuNotInCategoryException | CategoryNotInRestaurantException ee){
            return new ResponseEntity<>(Map.of(
                    "code",400,
                    "status",ee.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
        catch (Exception ee1){
            return new ResponseEntity<>(Map.of(
                    "code",400,
                    "status",ee1.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }

    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item")
    public ResponseEntity<Object> createItem(@PathVariable Long rest_id,@PathVariable Long cat_id,@PathVariable Long menu_id,@RequestBody ItemEntity addItem) throws Exception {
            try{
                ItemResource addResource = menuItemService.createItem(rest_id,cat_id,menu_id, addItem);
                return ResponseEntity.ok(Map.of(
                        "message","Menu Item created successfully",
                        "status",200

                ));
            }
            catch (MenuNotFoundException | RestaurantNotFoundException | CategoryNotFoundException e){
                return new ResponseEntity<>(Map.of(
                        "message",e.getMessage(),
                        "status",404
                ), HttpStatus.NOT_FOUND);
            }
            catch (MenuNotInCategoryException | CategoryNotInRestaurantException ee){
                return new ResponseEntity<>(
                        Map.of("message", ee.getMessage(),
                                "code", 400)
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
    @PutMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/{id}")
    public  ResponseEntity<Object> updateItem(@PathVariable Long rest_id,@PathVariable Long cat_id,@PathVariable Long menu_id,@PathVariable Long id, @RequestBody ItemEntity updatedItem) throws Exception {
            try {
                ItemResource updatedResource = menuItemService.updateItem(rest_id,cat_id,menu_id,id, updatedItem);
                return ResponseEntity.ok(Map.of(
                        "message", "Menu Item updated successfully",
                        "status", 200
                ));
            }
            catch (RestaurantNotFoundException | CategoryNotFoundException | MenuNotFoundException | ItemNotFoundException e){
                return new ResponseEntity<>(Map.of(
                        "message",e.getMessage(),
                        "status",404
                ), HttpStatus.NOT_FOUND);
            }
            catch (MenuNotInCategoryException | CategoryNotInRestaurantException | ItemNotInMenuException ee){
                return new ResponseEntity<>(
                        Map.of("message", ee.getMessage(),
                                "code", 400)
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
    @DeleteMapping("/restaurants/{rest_id}/category/{cat_id}/menus/{menu_id}/item/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long rest_id,@PathVariable Long cat_id,@PathVariable Long menu_id,@PathVariable Long id) throws Exception {
            try {
                menuItemService.deleteItem(rest_id,cat_id,menu_id,id);
                return ResponseEntity.ok(Map.of(
                        "message", "Menu deleted successfully",
                        "status", 200
                ));
            }
            catch (RestaurantNotFoundException | CategoryNotFoundException | MenuNotFoundException | ItemNotFoundException e){
                return new ResponseEntity<>(Map.of(
                        "message",e.getMessage(),
                        "status",404
                ), HttpStatus.NOT_FOUND);
            }
            catch (MenuNotInCategoryException | CategoryNotInRestaurantException | ItemNotInMenuException ee){
                return new ResponseEntity<>(
                        Map.of("message", ee.getMessage(),
                                "code", 400)
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
}
