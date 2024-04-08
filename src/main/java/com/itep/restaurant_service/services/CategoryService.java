package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryResource> getCategory(long rest_id) throws Exception;
    Optional<CategoryResource>  getCategoryDetails(long restaurantId, long categoryId ) throws Exception;
    CategoryResource createCategory(long restaurantId ,CategoryEntity body) throws Exception;

    CategoryResource updateCategory(long restaurantId ,long id, CategoryEntity body) throws Exception;

    void deleteCategory(long restaurantId ,long id) throws Exception;
}
