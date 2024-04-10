package com.itep.restaurant_service.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.MenuItemRepository;
import com.itep.restaurant_service.repositories.UserRepository;
import com.itep.restaurant_service.repositories.entities.*;
import com.itep.restaurant_service.security.WebSecurityConfig;
import com.itep.restaurant_service.services.impl.MenuItemServiceImpl;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(MenuItemController.class)
@ExtendWith(SpringExtension.class)
@Import(WebSecurityConfig.class)
public class MenuItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MenuItemRepository menuItemRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    private RestaurantServiceImpl restaurantService;
    @MockBean
    private MenuItemServiceImpl menuItemService;




    @Test
    @WithMockUser()
    public void testGetItems_Empty() throws Exception {
        List<ItemResource> result = new ArrayList<>();
        when(menuItemService.getItems(1,1,1))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/1/item"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));




    }

    @Test
    @WithMockUser()
    public void testGetItems_NotEmpty() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        List<ItemResource> result = new ArrayList<>();
        result.add(item.toItemResource());
        when(menuItemService.getItems(1,1,1))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/1/item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items",hasSize(1)))
                .andExpect(jsonPath("$.data.items[0].*",hasSize(6)))

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.items[0].id", equalTo(((int) result.get(0).getId()))))
                .andExpect(jsonPath("$.data.items[0].name", equalTo(result.get(0).getName())))
                .andExpect(jsonPath("$.data.items[0].description", equalTo(result.get(0).getDescription())))
                .andExpect(jsonPath("$.data.items[0].price", equalTo((double) result.get(0).getPrice())))
                .andExpect(jsonPath("$.data.items[0].quantity", equalTo((double) result.get(0).getQuantity())))
                .andExpect(jsonPath("$.data.items[0].menu", equalTo((int)result.get(0).getMenu())))

                ;




    }

    @Test
    @WithMockUser()
    void testGetItemDetail_NotFound() throws Exception {

        when(menuItemService.getItemsDetails(1,1,1,0))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/1/item/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetItemDetail_Found() throws Exception {
        Optional<ItemResource> result = getItemResource();
        when(menuItemService.getItemsDetails(1,1,1,1))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/1/item/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(6)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(((int) result.get().getId()))))
                .andExpect(jsonPath("$.name", equalTo(result.get().getName())))
                .andExpect(jsonPath("$.description", equalTo(result.get().getDescription())))
                .andExpect(jsonPath("$.quantity", equalTo((double) result.get().getQuantity())))
                .andExpect(jsonPath("$.price", equalTo((double) result.get().getPrice())))
                .andExpect(jsonPath("$.menu", equalTo((int)result.get().getMenu())))
        ;
    }

    private static Optional<ItemResource> getItemResource() {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        Optional<ItemResource> result = Optional.of(new ItemResource(1, "item 1",1200,500,"description", menu.getId()));
        return result;
    }

    @Test
    @WithMockUser()
    public void testCreateItem_noUser() throws Exception{
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1L,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/1/menus/1/item").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testCreateItem_restarantOwner() throws Exception{
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1L,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/1/menus/1/item").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser()
    public void testUpdateItem_noUser() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1L,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1/menus/1/item/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testUpdateItem_restauarentOwner() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1L,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1/menus/1/item/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    public void testDeleteItem_noUser() throws Exception{
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1L,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1/menus/1/item/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testDeleteItem_restauarentOwner() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1L,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1/menus/1/item/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isOk());
    }

}
