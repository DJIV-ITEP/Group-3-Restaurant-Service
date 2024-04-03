package com.itep.restaurant_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.UserRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import com.itep.restaurant_service.security.WebSecurityConfig;
import com.itep.restaurant_service.services.impl.CategoryServiceImpl;
import com.itep.restaurant_service.services.impl.MenuServiceImpl;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@ExtendWith(SpringExtension.class)
@Import(WebSecurityConfig.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CategoryRepository categoryRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    private RestaurantServiceImpl restaurantService;
    @MockBean
    private CategoryServiceImpl categoryService;


    @Test
    @WithMockUser()
    void testGetCategory_Empty() throws Exception {
        List<CategoryResource> result = new ArrayList<>();
        when(categoryService.getCategory(1))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser()
    void testGetCategory_NotEmpty() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1L, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1L,"name", restaurant,null);


        List<CategoryResource> result = new ArrayList<>();
        result.add(category.toCategoryResource());

        when(categoryService.getCategory(1))
                .thenReturn(result);
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].*",hasSize(3)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(((int) result.get(0).getId()))))
                .andExpect(jsonPath("$[0].name", equalTo(result.get(0).getName())))
                .andExpect(jsonPath("$[0].restaurant", equalTo((int) result.get(0).getRestaurant())))

                ;
    }
    @Test
    @WithMockUser()
    public void testCreateCategory_noUser() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testCreateCategory_restaurantOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    public void testUpdateCategory_notuser() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isForbidden());

    }


    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testUpdateCategory_restaurantOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser()
    public void testDeleteCategory_notuser() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isForbidden());

    }



    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testDeleteCategory_restaurantOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isOk());

    }




}
