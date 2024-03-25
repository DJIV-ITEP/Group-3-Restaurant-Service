package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;

import java.util.List;

public interface CategoryService {
    List<CategoryResource> getCategory(Long rest_id);
    CategoryResource createCategory(Long rest_id,CategoryEntity body) throws Exception;

    CategoryResource updateCategory(Long id, CategoryEntity body) throws Exception;

    void deleteCategory(Long id) throws Exception;
}
