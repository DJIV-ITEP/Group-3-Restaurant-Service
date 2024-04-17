package com.itep.restaurant_service.repositories;

import com.itep.restaurant_service.repositories.entities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest( showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AllRepositoriesInIT {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    private RestaurantEntity restaurant;
    private CategoryEntity category;
    private MenuEntity menu;
    private ItemEntity item;
    @BeforeEach
    public void setUp() {
        restaurant = new RestaurantEntity(1L, "R1", "address", "location", "offline", "seafood", "cuisine",
                new UserEntity("o1", "o1"),
                new ArrayList<>());
        restaurant = restaurantRepository.save(restaurant);
        category = new CategoryEntity(1L, "c1", restaurant, new ArrayList<>());
        category = categoryRepository.save(category);
        menu = new MenuEntity(1L, "m1", category, new ArrayList<>());
        menu = menuRepository.save(menu);
        item = new ItemEntity(1L, "i1", "this is for testing", 250,500, menu);
        item = menuItemRepository.save(item);
    }

    @AfterEach
    public void tearDown() {
        menuItemRepository.deleteAll();
        menuRepository.deleteAll();
        categoryRepository.deleteAll();
        restaurantRepository.deleteAll();
    }
    @Test
    void getRestaurantsAfterAddingOneRestaurant(){
        var restaurantEntity = restaurantRepository.findById(restaurant.getId());
        assertThat(restaurantEntity).isPresent();
        assertThat(restaurantEntity.get().getId()).isEqualTo(restaurant.getId());
    }
    @Test
    void setRestaurantStatus(){
        restaurant.setStatus("online");
        assertThat(restaurantRepository.save(restaurant).getStatus()).isEqualTo("online");
    }
    @Test
    void getAvailableRestaurantsAfterSettingRestaurantStatus(){
        restaurant.setStatus("online");
        restaurantRepository.save(restaurant);
        var availableRestaurants = restaurantRepository.findByStatus("online");
        assertThat(availableRestaurants).hasSize(1);
        assertThat(availableRestaurants.get(0).getId()).isEqualTo(restaurant.getId());

    }
    @Test
    void getRestaurantCategoriesAfterAddingOneCategory(){
        var restaurantCategories = categoryRepository.findByRestaurantId(restaurant.getId());
        assertThat(restaurantCategories).hasSize(1);
        assertThat(restaurantCategories.get(0).getRestaurant().getId()).isEqualTo(category.getRestaurant().getId());
        assertThat(restaurantCategories.get(0).getId()).isEqualTo(category.getId());
    }
    @Test
    void getRestaurantCategoriesAfterUpdatingOneCategory(){
        category.setName("category");
        category = categoryRepository.save(category);
        var restaurantCategories = categoryRepository.findByRestaurantId(restaurant.getId());
        assertThat(restaurantCategories).hasSize(1);
        assertThat(restaurantCategories.get(0).getRestaurant().getId()).isEqualTo(category.getRestaurant().getId());
        assertThat(restaurantCategories.get(0).getId()).isEqualTo(category.getId());
        assertThat(restaurantCategories.get(0).getName()).isEqualTo("category");
    }
    @Test
    void getRestaurantCategoriesAfterDeletingOneCategory(){
        // here we should delete all related entities before
        menuItemRepository.delete(item);
        menuRepository.delete(menu);
        categoryRepository.delete(category);
        var restaurantCategories = categoryRepository.findByRestaurantId(restaurant.getId());
        assertThat(restaurantCategories).hasSize(0);
    }
    @Test
    void getRestaurantCategoriesMenusAfterAddingOneMenu(){
        var restaurantCategoriesMenus = menuRepository.findByCategoryId(category.getId());
        assertThat(restaurantCategoriesMenus).hasSize(1);
        assertThat(restaurantCategoriesMenus.get(0).getCategory().getRestaurant().getId()).isEqualTo(menu.getCategory().getRestaurant().getId());
        assertThat(restaurantCategoriesMenus.get(0).getCategory().getId()).isEqualTo(menu.getCategory().getId());
        assertThat(restaurantCategoriesMenus.get(0).getId()).isEqualTo(menu.getId());
    }
    @Test
    void getRestaurantCategoriesMenusAfterUpdatingOneMenu(){
        menu.setName("menus");
        menu = menuRepository.save(menu);
        var restaurantCategoriesMenus = menuRepository.findByCategoryId(category.getId());
        assertThat(restaurantCategoriesMenus).hasSize(1);
        assertThat(restaurantCategoriesMenus.get(0).getCategory().getRestaurant().getId()).isEqualTo(menu.getCategory().getRestaurant().getId());
        assertThat(restaurantCategoriesMenus.get(0).getCategory().getId()).isEqualTo(menu.getCategory().getId());
        assertThat(restaurantCategoriesMenus.get(0).getId()).isEqualTo(menu.getId());
        assertThat(restaurantCategoriesMenus.get(0).getName()).isEqualTo("menus");
    }
    @Test
    void getRestaurantCategoriesMenusAfterDeletingOneMenu(){
        // here we should delete all related entities before
        menuItemRepository.delete(item);
        menuRepository.delete(menu);
        var restaurantCategoriesMenus = menuRepository.findByCategoryId(category.getId());
        assertThat(restaurantCategoriesMenus).hasSize(0);
    }
    @Test
    void getRestaurantCategoriesMenusItemsAfterAddingOneItem(){
        var restaurantCategoriesMenusItem = menuItemRepository.findByMenuId(menu.getId());
        assertThat(restaurantCategoriesMenusItem).hasSize(1);
        assertThat(restaurantCategoriesMenusItem.get(0).getMenu().getCategory().getRestaurant().getId()).isEqualTo(item.getMenu().getCategory().getRestaurant().getId());
        assertThat(restaurantCategoriesMenusItem.get(0).getMenu().getCategory().getId()).isEqualTo(item.getMenu().getCategory().getId());
        assertThat(restaurantCategoriesMenusItem.get(0).getMenu().getId()).isEqualTo(item.getMenu().getId());
        assertThat(restaurantCategoriesMenusItem.get(0).getId()).isEqualTo(item.getId());
    }

    @Test
    void getRestaurantCategoriesMenusItemsAfterUpdatingOneItem(){
        item.setName("item");
        item = menuItemRepository.save(item);
        var restaurantCategoriesMenusItem = menuItemRepository.findByMenuId(menu.getId());
        assertThat(restaurantCategoriesMenusItem).hasSize(1);
        assertThat(restaurantCategoriesMenusItem.get(0).getMenu().getCategory().getRestaurant().getId()).isEqualTo(item.getMenu().getCategory().getRestaurant().getId());
        assertThat(restaurantCategoriesMenusItem.get(0).getMenu().getCategory().getId()).isEqualTo(item.getMenu().getCategory().getId());
        assertThat(restaurantCategoriesMenusItem.get(0).getMenu().getId()).isEqualTo(item.getMenu().getId());
        assertThat(restaurantCategoriesMenusItem.get(0).getId()).isEqualTo(item.getId());
        assertThat(restaurantCategoriesMenusItem.get(0).getName()).isEqualTo("item");
    }
    @Test
    void getRestaurantCategoriesMenusItemsAfterDeletingOneItem(){
        menuItemRepository.delete(item);
        var restaurantCategoriesMenusItem = menuItemRepository.findByMenuId(menu.getId());
        assertThat(restaurantCategoriesMenusItem).hasSize(0);
    }

}

