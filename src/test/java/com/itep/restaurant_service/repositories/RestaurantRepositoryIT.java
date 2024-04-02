package com.itep.restaurant_service.repositories;

import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RestaurantRepositoryIT {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @Transactional
    void setRestaurantStatus(){
        restaurantRepository.save(new RestaurantEntity(1L, "R1", "address", "location", "offline", "seafood", "cuisine",
                new UserEntity("o1", "o1"),
                new ArrayList<>()));
        restaurantRepository.updateStatus(1L,"online");
        var restaurant = restaurantRepository.findById(1L);
        System.out.println(restaurant);
        assertThat(restaurant).isPresent();
        assertThat(restaurant.get().getStatus()).isEqualTo("online");
    }
    @Test
    void getAvailableRestaurantsAfterSettingRestaurantStatus(){
        var r1 = new RestaurantEntity(1, "R1", "address", "location", "offline", "seafood", "cuisine",
                new UserEntity("o1", "o1"),
                new ArrayList<>());
        restaurantRepository.save(r1);
        var r2 = new RestaurantEntity(2, "R2", "address", "location", "offline", "vegan", "cuisine",
                new UserEntity("o2", "o2"),
                new ArrayList<>());
        restaurantRepository.save(r2);
        restaurantRepository.updateStatus(r2.getId(),"online");
        var availableRestaurants = restaurantRepository.findByStatus("online");
        assertThat(availableRestaurants).hasSize(1);
        assertThat(availableRestaurants.get(0).getId()).isEqualTo(r2.getId());
    }
    @Test
    void getRestaurantCategoriesAfterAddingOneCategory(){
        var r1 = new RestaurantEntity(1, "R1", "address", "location", "offline", "seafood", "cuisine",
                new UserEntity("o1", "o1"),
                new ArrayList<>());
        var c1 = new CategoryEntity(1, "c1", r1, new ArrayList<>());
        restaurantRepository.save(r1);
        categoryRepository.save(c1);
        var restaurantCategories = categoryRepository.findByRestaurantId(1L);
        assertThat(restaurantCategories).hasSize(1);
        assertThat(restaurantCategories.get(0).getRestaurant().getId()).isEqualTo(c1.getRestaurant().getId());
        assertThat(restaurantCategories.get(0).getId()).isEqualTo(c1.getId());
    }
}

