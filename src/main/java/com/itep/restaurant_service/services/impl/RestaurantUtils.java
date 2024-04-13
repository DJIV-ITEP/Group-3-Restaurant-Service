package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;

public class RestaurantUtils {
    public static boolean isCategoryInRestaurant(CategoryEntity categoryEntity, long restaurantId) {
        return Long.valueOf(categoryEntity.getRestaurant().getId()).equals(restaurantId);
    }
    public static boolean isMenuInCategory(MenuEntity menuEntity, long categoryId) {
        return Long.valueOf(menuEntity.getCategory().getId()).equals(categoryId);
    }
    public static boolean isItemInMenu(ItemEntity itemEntity, long menuId) {
        return Long.valueOf(itemEntity.getMenu().getId()).equals(menuId);
    }
    public static boolean isRestaurantOwner(RestaurantEntity restaurantEntity, String name) {
        return (restaurantEntity.getOwner().getUsername()).equals(name);
    }
}
