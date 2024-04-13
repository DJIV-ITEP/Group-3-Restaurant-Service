package com.itep.restaurant_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.models.RestaurantResource;
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
import com.itep.restaurant_service.services.impl.errorsHandels.CategoryNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.CategoryNotInRestaurantException;
import com.itep.restaurant_service.services.impl.errorsHandels.RestaurantNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.UserNotOwnerOfRestaurantException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
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
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        CategoryEntity category2 = new CategoryEntity(2,"name 2", restaurant,null);

        List<CategoryResource> result = new ArrayList<>();
        result.add(category.toCategoryResource());
        result.add(category2.toCategoryResource());

        when(categoryService.getCategory(1))
                .thenReturn(result);
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items",hasSize(2)))
                .andExpect(jsonPath("$.data.items[0].*",hasSize(3)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.items[0].id", equalTo(((int) result.get(0).getId()))))
                .andExpect(jsonPath("$.data.items[0].name", equalTo(result.get(0).getName())))
                .andExpect(jsonPath("$.data.items[0].restaurant", equalTo((int) result.get(0).getRestaurant())))

                ;
    }

    @Test
    @WithMockUser()
    void testGetCategory_NotEmptyRestaurantNotFound() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        CategoryEntity category2 = new CategoryEntity(2,"name 2", restaurant,null);

        List<CategoryResource> result = new ArrayList<>();


        when(categoryService.getCategory(0))
                .thenThrow(new RestaurantNotFoundException(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/0/category"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))



        ;
    }
    @Test
    @WithMockUser()
    void testGetCategoryDetail_NotFound() throws Exception {

        when(categoryService.getCategoryDetails(1,0))
                .thenThrow(new CategoryNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetCategoryDetail_RestaurentNotFound() throws Exception {

        when(categoryService.getCategoryDetails(0,1))
                .thenThrow(new RestaurantNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/0/category/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetCategoryDetail_CategoryNotInRestaurent() throws Exception {

        when(categoryService.getCategoryDetails(1,5))
                .thenThrow(new CategoryNotInRestaurantException(5));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/5"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetCategoryDetail_Found() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryResource result = new CategoryResource(1, "name", restaurant.getId());
        when(categoryService.getCategoryDetails(1,1))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.item.*", hasSize(3)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.item.id", equalTo(((int) result.getId()))))
                .andExpect(jsonPath("$.data.item.name", equalTo(result.getName())))
                .andExpect(jsonPath("$.data.item.restaurant", equalTo((int) result.getRestaurant())))
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
         mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                        .andExpect(status().isOk()
                );
    }

    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aa",password = "123")
    public void testCreateCategory_restaurantNotOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        when(categoryService.createCategory(1,category))
                .thenThrow(new UserNotOwnerOfRestaurantException());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))

                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aa",password = "123")
    public void testCreateCategory_restaurantNotFound() throws Exception {

        CategoryEntity category = new CategoryEntity(1,"name", null,null);
        when(categoryService.createCategory(0,category))
                .thenThrow(new RestaurantNotFoundException(0));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/0/category")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isNotFound())
                ;

    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testCreateCategory_missingValue() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,null, restaurant,null);
        when(categoryService.createCategory(1,category))
                .thenThrow(new Exception("You must provide all the category fields"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testCreateCategory_alreadyExist() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category0 = new CategoryEntity(1,"name", restaurant,null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        when(categoryService.createCategory(1,category))
                .thenThrow(new Exception("category with this name already exists"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());

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
    @WithMockUser(roles = "RESTAURANT",username = "aaa",password = "123")
    public void testUpdateCategory_restaurantNotOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        when(categoryService.updateCategory(1,1,category))
                .thenThrow(new UserNotOwnerOfRestaurantException());
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
    public void testUpdateCategory_restaurantNotFound() throws Exception {

        CategoryEntity category = new CategoryEntity(1,"name", null,null);
        when(categoryService.updateCategory(0,1,category))
                .thenThrow(new RestaurantNotFoundException(0));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/0/category/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isNotFound());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aaa",password = "123")
    public void testUpdateCategory_categoryNotFound() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        when(categoryService.updateCategory(1,0,category))
                .thenThrow(new CategoryNotFoundException(0));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/0").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isNotFound());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aaa",password = "123")
    public void testUpdateCategory_missingValue() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        when(categoryService.updateCategory(1,1,category))
                .thenThrow(new Exception("name is required"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testUpdateCategory_CatNotInRest() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        when(categoryService.updateCategory(1,5,category))
                .thenThrow(new CategoryNotInRestaurantException(5));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/5").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());

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
    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aaa",password = "123")
    public void testDeleteCategory_restaurantNotOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        doThrow(new UserNotOwnerOfRestaurantException()).when(categoryService).deleteCategory(1, 1);

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
    public void testDeleteCategory_restaurantNotFound() throws Exception {

        CategoryEntity category = new CategoryEntity(1,"name", null,null);
        doThrow(new RestaurantNotFoundException(0)).when(categoryService).deleteCategory(0, 1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/0/category/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isNotFound());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aaa",password = "123")
    public void testDeleteCategory_categoryNotFound() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        doThrow(new CategoryNotFoundException(0)).when(categoryService).deleteCategory(1, 0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/0").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isNotFound());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testDeleteCategory_CatNotInRest() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        doThrow(new CategoryNotInRestaurantException(1)).when(categoryService).deleteCategory(1, 5);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(category);
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/5").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());

    }


}
