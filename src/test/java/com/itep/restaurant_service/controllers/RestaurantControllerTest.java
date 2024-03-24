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
    void testCreateRestaurant_NotSystemAdmin() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine","username", "password", new AdminEntity("", ""));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(restaurant);

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles= "ADMIN")
    void testCreateRestaurant_SystemAdmin() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine","username", "password", null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(restaurant);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser()
    void testGetRestaurantDetail_NotFound() throws Exception {

        when(restaurantService.getRestaurantDetails(0))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetRestaurantDetail_Found() throws Exception {
        Optional<RestaurantResource> result = Optional.of(new RestaurantResource(1, "name", "address", "location", "status", "food", "cuisine"));
        when(restaurantService.getRestaurantDetails(1))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(7)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(((int) result.get().id))))
                .andExpect(jsonPath("$.name", equalTo(result.get().name)))
                .andExpect(jsonPath("$.address", equalTo(result.get().address)))
                .andExpect(jsonPath("$.location", equalTo(result.get().location)))
                .andExpect(jsonPath("$.status", equalTo(result.get().status)))
                .andExpect(jsonPath("$.food", equalTo(result.get().food)))
                .andExpect(jsonPath("$.cuisine", equalTo(result.get().cuisine)))
        ;
    }
}
