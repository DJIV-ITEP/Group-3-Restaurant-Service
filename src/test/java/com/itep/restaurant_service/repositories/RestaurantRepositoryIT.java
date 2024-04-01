package com.itep.restaurant_service.repositories;

import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RestaurantRepositoryIT {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @BeforeEach
    public void setup() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "online", "food", "cuisine", new UserEntity("owner", "owner"), new ArrayList<>());
        restaurantRepository.save(restaurant);
    }
    @Test
    void setRestaurantStatus(){
        restaurantRepository.updateStatus(1,"online");
        var restaurant = restaurantRepository.findById(1L);
        assertThat(restaurant).isPresent();
        assertThat(restaurant.get().getStatus()).isEqualTo("online");
    }
}
