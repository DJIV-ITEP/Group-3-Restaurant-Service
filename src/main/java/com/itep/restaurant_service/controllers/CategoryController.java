package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.services.impl.CategoryServiceImpl;
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
    public ResponseEntity<Object> getCategory(@PathVariable long restaurantId){

        List<CategoryResource> result = categoryService.getCategory(restaurantId);
        if(!result.isEmpty()){
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(
                    Map.of("message","no Category found for this restaurant", "status",200)
                    , HttpStatus.OK
            );
        }

    }

    @GetMapping("/restaurants/{restaurantId}/category/{categoryId}")
    public ResponseEntity<Object> getCategory(@PathVariable long restaurantId,@PathVariable long categoryId){
        var categoryResource = categoryService.getCategoryDetails(restaurantId,categoryId);
        return categoryResource.<ResponseEntity<Object>>map(
                        resource -> new ResponseEntity<>(
                                resource, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        Map.of("message", "Category not found",
                                "status", 404)
                        , HttpStatus.NOT_FOUND));

    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/restaurants/{restaurantId}/category")
    public ResponseEntity<Object> createCategory(@PathVariable long restaurantId , @RequestBody CategoryEntity addCategory) throws Exception {

        CategoryResource addResource = categoryService.createCategory(restaurantId,addCategory);
            return ResponseEntity.ok(Map.of(
                    "message","Category created successfully",
                    "status",200
            ));



    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping("/restaurants/{restaurantId}/category/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable long restaurantId,@PathVariable long id, @RequestBody CategoryEntity updatedCategory) throws Exception {
            CategoryResource updatedResource = categoryService.updateCategory(restaurantId, id, updatedCategory);
            return ResponseEntity.ok(Map.of(
                    "message", "Category updated successfully",
                    "status", 200
            ));

    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @DeleteMapping("/restaurants/{restaurantId}/category/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long restaurantId,@PathVariable long id) throws Exception {
        categoryService.deleteCategory(restaurantId, id);
        return ResponseEntity.ok(Map.of(
                "message", "Category deleted successfully",
                "status", 200
        ));
    }

}
