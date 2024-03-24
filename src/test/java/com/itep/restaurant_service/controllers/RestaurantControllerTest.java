package com.itep.restaurant_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.AdminRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.AdminEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.security.WebSecurityConfig;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RestaurantController.class)
@ExtendWith(SpringExtension.class)
@Import(WebSecurityConfig.class)
public class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    AdminRepository adminRepository;

    @MockBean
    RestaurantRepository restaurantRepository;

    @MockBean
    private RestaurantServiceImpl restaurantService;

    @Test
    @WithMockUser()
    void testGetAvailableRestaurants_Empty() throws Exception {
        List<RestaurantResource> result = new ArrayList<>();
        when(restaurantService.getAvailableRestaurants())
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetAvailableRestaurants_NotEmpty() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine","username", "password", new AdminEntity("admin", "admin"));
        List<RestaurantResource> result = new ArrayList<>();
        result.add(restaurant.toRestaurantResource());
        when(restaurantService.getAvailableRestaurants())
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].*", hasSize(7)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(((int) result.get(0).id))))
                .andExpect(jsonPath("$[0].name", equalTo(result.get(0).name)))
                .andExpect(jsonPath("$[0].address", equalTo(result.get(0).address)))
                .andExpect(jsonPath("$[0].location", equalTo(result.get(0).location)))
                .andExpect(jsonPath("$[0].status", equalTo(result.get(0).status)))
                .andExpect(jsonPath("$[0].food", equalTo(result.get(0).food)))
                .andExpect(jsonPath("$[0].cuisine", equalTo(result.get(0).cuisine)))
        ;
    }
    @Test
    @WithMockUser()
    void testGetAvailableFilteredRestaurants_ByFoodAndCuisine() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni","username", "password", new AdminEntity("admin", "admin"));
        RestaurantEntity restaurant2 = new RestaurantEntity(2, "name", "address", "location", "status", "Vegan", "Yemeni","username", "password", new AdminEntity("admin", "admin"));
        RestaurantEntity restaurant3 = new RestaurantEntity(3, "name", "address", "location", "status", "Seafood", "Egyptian","username", "password", new AdminEntity("admin", "admin"));
        RestaurantEntity restaurant4 = new RestaurantEntity(4, "name", "address", "location", "status", "Vegan", "Egyptian","username", "password", new AdminEntity("admin", "admin"));
        List<RestaurantResource> result = new ArrayList<>();
        result.add(restaurant1.toRestaurantResource());
        when(restaurantService.getAvailableFilteredRestaurants("Seafood", "Yemeni"))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants")
                        .param("food","Seafood")
                        .param("cuisine", "Yemeni"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].*", hasSize(7)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(((int) result.get(0).id))))
                .andExpect(jsonPath("$[0].name", equalTo(result.get(0).name)))
                .andExpect(jsonPath("$[0].address", equalTo(result.get(0).address)))
                .andExpect(jsonPath("$[0].location", equalTo(result.get(0).location)))
                .andExpect(jsonPath("$[0].status", equalTo(result.get(0).status)))
                .andExpect(jsonPath("$[0].food", equalTo(result.get(0).food)))
                .andExpect(jsonPath("$[0].cuisine", equalTo(result.get(0).cuisine)))
        ;
    }

    @Test
    @WithMockUser()
    void testGetAvailableFilteredRestaurants_ByFood() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni","username", "password", new AdminEntity("admin", "admin"));
        RestaurantEntity restaurant2 = new RestaurantEntity(2, "name", "address", "location", "status", "Vegan", "Yemeni","username", "password", new AdminEntity("admin", "admin"));
        RestaurantEntity restaurant3 = new RestaurantEntity(3, "name", "address", "location", "status", "Seafood", "Egyptian","username", "password", new AdminEntity("admin", "admin"));
        RestaurantEntity restaurant4 = new RestaurantEntity(4, "name", "address", "location", "status", "Vegan", "Egyptian","username", "password", new AdminEntity("admin", "admin"));
        List<RestaurantResource> result = new ArrayList<>();
        result.add(restaurant1.toRestaurantResource());
        result.add(restaurant3.toRestaurantResource());
        when(restaurantService.getAvailableFilteredRestaurants("Seafood",  null))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants")
                        .param("food","Seafood"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].*", hasSize(7)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(((int) result.get(0).id))))
                .andExpect(jsonPath("$[0].name", equalTo(result.get(0).name)))
                .andExpect(jsonPath("$[0].address", equalTo(result.get(0).address)))
                .andExpect(jsonPath("$[0].location", equalTo(result.get(0).location)))
                .andExpect(jsonPath("$[0].status", equalTo(result.get(0).status)))
                .andExpect(jsonPath("$[0].food", equalTo(result.get(0).food)))
                .andExpect(jsonPath("$[0].cuisine", equalTo(result.get(0).cuisine)))
        ;
    }

    @Test
    @WithMockUser()
    void testGetAvailableFilteredRestaurants_ByCuisine() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni","username", "password", new AdminEntity("admin", "admin"));
        RestaurantEntity restaurant2 = new RestaurantEntity(2, "name", "address", "location", "status", "Vegan", "Yemeni","username", "password", new AdminEntity("admin", "admin"));
        RestaurantEntity restaurant3 = new RestaurantEntity(3, "name", "address", "location", "status", "Seafood", "Egyptian","username", "password", new AdminEntity("admin", "admin"));
        RestaurantEntity restaurant4 = new RestaurantEntity(4, "name", "address", "location", "status", "Vegan", "Egyptian","username", "password", new AdminEntity("admin", "admin"));
        List<RestaurantResource> result = new ArrayList<>();
        result.add(restaurant1.toRestaurantResource());
        result.add(restaurant2.toRestaurantResource());
        when(restaurantService.getAvailableFilteredRestaurants(null,  "Yemeni"))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants")
                        .param("cuisine","Yemeni"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].*", hasSize(7)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(((int) result.get(0).id))))
                .andExpect(jsonPath("$[0].name", equalTo(result.get(0).name)))
                .andExpect(jsonPath("$[0].address", equalTo(result.get(0).address)))
                .andExpect(jsonPath("$[0].location", equalTo(result.get(0).location)))
                .andExpect(jsonPath("$[0].status", equalTo(result.get(0).status)))
                .andExpect(jsonPath("$[0].food", equalTo(result.get(0).food)))
                .andExpect(jsonPath("$[0].cuisine", equalTo(result.get(0).cuisine)))
        ;
    }



}
