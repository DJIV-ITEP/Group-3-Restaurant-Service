package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.services.impl.CategoryServiceImpl;
import com.itep.restaurant_service.services.impl.MenuServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/restaurants/{rest_id}/category")
    public List<CategoryResource> getCategory(@PathVariable Long rest_id){
        return categoryService.getCategory(rest_id);
    }
    @PostMapping("/restaurants/{rest_id}/category/create")
    public ResponseEntity<Object> createMenu(@RequestBody CategoryEntity addCatogery){
        try {
            CategoryResource addResource = categoryService.createCategory(addCatogery);
            return ResponseEntity.ok(Map.of(
                    "message","Category created successfully",
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
    @PutMapping("/restaurants/{rest_id}/category/update/{id}")
    public ResponseEntity<Object> updateMenu(@PathVariable Long id, @RequestBody CategoryEntity updatedCategory) {
        try {
            CategoryResource updatedResource = categoryService.updateCategory(id, updatedCategory);
            return ResponseEntity.ok(Map.of(
                    "message", "Category updated successfully",
                    "status", 200,
                    "data", updatedResource
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("message", e.getMessage(), "status", 400)
            );
        }
    }

    @DeleteMapping("/restaurants/{rest_id}/category/delete/{id}")
    public ResponseEntity<Object> deleteCatogery(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Category deleted successfully",
                    "status", 200
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("message", e.getMessage(), "status", 400)
            );
        }
    }
}
