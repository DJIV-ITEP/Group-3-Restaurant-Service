package com.itep.restaurant_service.services;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.UserRepository;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
public class RestaurantServiceTest {

    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    RestaurantServiceImpl restaurantService;

    void assertAvailableRestaurants(List<RestaurantResource> restaurantResources, int size){
        assertThat(restaurantResources)
                .hasSize(size)
                .element(0)
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("name")
                .hasFieldOrProperty("address")
                .hasFieldOrProperty("location")
                .hasFieldOrProperty("status")
                .hasFieldOrProperty("food")
                .hasFieldOrProperty("cuisine");

    }

    @Test
    void testGetAvailableRestaurants_Empty() {
        given(restaurantRepository.findByStatus("online"))
                .willReturn(List.of());

        assertThat(restaurantService.getAvailableRestaurants())
                .hasSize(0);
    }
    @Test
    void testGetAvailableRestaurants_NotEmpty() {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "online", "food", "cuisine", new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findByStatus("online"))
                .willReturn(List.of(restaurant));
        assertAvailableRestaurants(restaurantService.getAvailableRestaurants(),1);
    }
    @Test
    void testGetAvailableFilteredRestaurants_ByFoodAndCuisine() {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findByFoodAndCuisineAndStatus("Seafood","Yemeni", "online"))
                .willReturn(List.of(restaurant1));
        assertAvailableRestaurants(restaurantService.getAvailableFilteredRestaurantsByFoodAndCuisine("Seafood","Yemeni"),1);

    }
    @Test
    void testGetAvailableFilteredRestaurants_ByFood() {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        RestaurantEntity restaurant3 = new RestaurantEntity(3, "name", "address", "location", "status", "Seafood", "Egyptian",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findByFoodAndStatus("Seafood", "online"))
                .willReturn(List.of(restaurant1, restaurant3));
        assertAvailableRestaurants(restaurantService.getAvailableFilteredRestaurantsByFood("Seafood"),2);
    }
    @Test
    void testGetAvailableFilteredRestaurants_ByCuisine() {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        RestaurantEntity restaurant2 = new RestaurantEntity(2, "name", "address", "location", "status", "Vegan", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findByCuisineAndStatus("Yemeni", "online"))
                .willReturn(List.of(restaurant1, restaurant2));
        assertAvailableRestaurants(restaurantService.getAvailableFilteredRestaurantsByCuisine("Yemeni"),2);

    }
    @Test
    void testCreateRestaurant_Success() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.save(restaurant1))
                .willReturn(restaurant1);
        assertThat(restaurantService.createRestaurant(restaurant1))
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("name")
                .hasFieldOrProperty("address")
                .hasFieldOrProperty("location")
                .hasFieldOrProperty("status")
                .hasFieldOrProperty("food")
                .hasFieldOrProperty("cuisine");
    }
    @Test
    void testCreateRestaurant_Duplicate() {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        restaurantRepository.save(restaurant1);
        given(restaurantRepository.save(restaurant1))
                .willReturn(restaurant1);
        RestaurantEntity restaurant2 = new RestaurantEntity(2, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        restaurantRepository.save(restaurant2);
        given(restaurantRepository.save(restaurant2))
                .willThrow(new RuntimeException("duplicate key value violates unique constraint"));
        Throwable exception = assertThrows(Exception.class, () -> {
            restaurantService.createRestaurant(restaurant2);
        });
        assertThat(exception.getMessage())
                .isEqualTo("restaurant with this username already exists");

    }
    @Test
    void testCreateRestaurant_MissingValues() {
        RestaurantEntity restaurant2 = new RestaurantEntity(2, null, "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        restaurantRepository.save(restaurant2);
        given(restaurantRepository.save(restaurant2))
                .willThrow(new RuntimeException("not-null property references a null"));
        Throwable exception = assertThrows(Exception.class, () -> {
            restaurantService.createRestaurant(restaurant2);
        });
        assertThat(exception.getMessage())
                .isEqualTo("You must provide all the restaurant fields");
    }

    @Test
    void testGetRestaurantDetail_NotFound() {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());

        assertThat(restaurantService.getRestaurantDetails(1L))
                .isEmpty();
    }
    @Test
    void testGetRestaurantDetail_Found() {
        RestaurantEntity restaurant2 = new RestaurantEntity(2, null, "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);

        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant2));

        assertThat(restaurantService.getRestaurantDetails(1L))
                .isNotEmpty();
    }
    @Test
    void testGetRestaurantEntity_NotFound() {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());

        assertThat(restaurantService.getRestaurantEntity(1L))
                .isEmpty();
    }
    @Test
    void testGetRestaurantEntity_Found() {
        RestaurantEntity restaurant2 = new RestaurantEntity(2, null, "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);

        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant2));

        assertThat(restaurantService.getRestaurantEntity(1L))
                .isNotEmpty();
    }
    @Test
    void testGetRestaurantOwner_Found() {
        RestaurantEntity restaurant2 = new RestaurantEntity(2, null, "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);

        given(restaurantRepository.findOwnerById(1L))
                .willReturn(Optional.of(restaurant2));

        assertThat(restaurantService.getRestaurantOwner(1L))
                .isNotEmpty()
                .get()
                .returns("owner", UserEntity::getUsername)
                .returns("owner", UserEntity::getPassword);
    }
    @Test
    void testGetRestaurantUser_NotFound() {
        given(userRepository.findById("owner"))
                .willReturn(Optional.empty());

        assertThat(restaurantService.getRestaurantUserByUsername("owner"))
                .isNull();
    }
    @Test
    void testGetRestaurantUser_Found() {
        given(userRepository.findById("owner"))
                .willReturn(Optional.of(new UserEntity("owner", "owner")));
        var user = restaurantService.getRestaurantUserByUsername("owner");
        assertThat(user)
                .returns("owner", UserDetails::getUsername)
                .returns("owner", UserDetails::getUsername);
        assertThat(user.getAuthorities())
                .singleElement()
                .returns("ROLE_RESTAURANT", GrantedAuthority::getAuthority);
    }
    @Test
    void testGetAdminUser_Found() {
        given(userRepository.findById("admin"))
                .willReturn(Optional.of(new UserEntity("admin", "admin")));
        var user = restaurantService.getRestaurantUserByUsername("admin");
        assertThat(user)
                .returns("admin", UserDetails::getUsername)
                .returns("admin", UserDetails::getUsername);
        assertThat(user.getAuthorities())
                .singleElement()
                .returns("ROLE_ADMIN", GrantedAuthority::getAuthority);

    }
    @Test
    void testSetRestaurantStatus_Success() {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.save(restaurant1))
                .willReturn(restaurant1);
        restaurantService.setRestaurantStatus(restaurant1);
    }
}
