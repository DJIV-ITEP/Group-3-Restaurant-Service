package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.services.impl.CategoryServiceImpl;
import com.itep.restaurant_service.services.impl.errorsHandels.CategoryNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.CategoryNotInRestaurantException;
import com.itep.restaurant_service.services.impl.errorsHandels.RestaurantNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.UserNotOwnerOfRestaurantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/restaurants/{restaurantId}/category")
    public ResponseEntity<Object> getCategory(@PathVariable long restaurantId) throws Exception {
        try {
            List<CategoryResource> result = categoryService.getCategory(restaurantId);
            return new ResponseEntity<>(Map.of(
                    "code",200,
                    "data",Map.of("items",result)),HttpStatus.OK);
        } catch (RestaurantNotFoundException e) {
            return new ResponseEntity<>(
                    Map.of("message",e.getMessage(), "code",404)
                    , HttpStatus.NOT_FOUND
            );

        }


    }

    @GetMapping("/restaurants/{restaurantId}/category/{categoryId}")
    public ResponseEntity<Object> getCategory(@PathVariable long restaurantId,@PathVariable long categoryId) throws Exception {
        try{
            CategoryResource categoryResource = categoryService.getCategoryDetails(restaurantId,categoryId);
            return new ResponseEntity<>(Map.of("code",200,
                    "data",Map.of("item",categoryResource)),HttpStatus.OK);
        }
        catch (RestaurantNotFoundException | CategoryNotFoundException | CategoryNotInRestaurantException e){
            return new ResponseEntity<>(
                    Map.of("message",e.getMessage(), "code",404)
                    , HttpStatus.NOT_FOUND
            );
        }


    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/restaurants/{restaurantId}/category")
    public ResponseEntity<Object> createCategory(@PathVariable long restaurantId , @RequestBody CategoryEntity addCategory) throws Exception {
        try{
            CategoryResource addResource = categoryService.createCategory(restaurantId,addCategory);
            return ResponseEntity.ok(Map.of(
                    "message","Category created successfully",
                    "status",200
            ));
        }
        catch (RestaurantNotFoundException  e){
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
        catch (Exception ex){
            return new ResponseEntity<>(
                    Map.of("message",ex.getMessage(), "code",400)
                    , HttpStatus.BAD_REQUEST
            );
        }




    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping("/restaurants/{restaurantId}/category/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable long restaurantId,@PathVariable long id, @RequestBody CategoryEntity updatedCategory) throws Exception {
        try{
            CategoryResource updatedResource = categoryService.updateCategory(restaurantId, id, updatedCategory);
            return ResponseEntity.ok(Map.of(
                    "message", "Category updated successfully",
                    "status", 200
            ));
        }
        catch (RestaurantNotFoundException | CategoryNotFoundException e){
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
        catch (Exception ex){
            return new ResponseEntity<>(
                    Map.of("message",ex.getMessage(), "code",400)
                    , HttpStatus.BAD_REQUEST
            );
        }


    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @DeleteMapping("/restaurants/{restaurantId}/category/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long restaurantId,@PathVariable long id) throws Exception {
        try{
            categoryService.deleteCategory(restaurantId, id);
            return ResponseEntity.ok(Map.of(
                    "message", "Category deleted successfully",
                    "status", 200
            ));
        }
        catch (RestaurantNotFoundException | CategoryNotFoundException e){
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
        catch (Exception ex){
            return new ResponseEntity<>(
                    Map.of("message",ex.getMessage(), "code",400)
                    , HttpStatus.BAD_REQUEST
            );
        }
    }

}
