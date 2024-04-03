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
    @GetMapping("/restaurants/{rest_id}/category")
    public List<CategoryResource> getCategory(@PathVariable Long rest_id){
        return categoryService.getCategory(rest_id);
    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/restaurants/{rest_id}/category")
    public ResponseEntity<Object> createCategory(@PathVariable Long rest_id , @RequestBody CategoryEntity addCategory){
        try {


            CategoryResource addResource = categoryService.createCategory(rest_id,addCategory);
            return ResponseEntity.ok(Map.of(
                    "message","Category created successfully",
                    "status",200
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
    @PutMapping("/restaurants/{rest_id}/category/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long rest_id,@PathVariable Long id, @RequestBody CategoryEntity updatedCategory) {
        try {
            CategoryResource updatedResource = categoryService.updateCategory(rest_id, id, updatedCategory);
            return ResponseEntity.ok(Map.of(
                    "message", "Category updated successfully",
                    "status", 200
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("message", e.getMessage(), "status", 400)
            );
        }
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @DeleteMapping("/restaurants/{rest_id}/category/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long rest_id,@PathVariable long id) {
        try {
            categoryService.deleteCategory(rest_id,id);
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
