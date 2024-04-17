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
import com.itep.restaurant_service.services.impl.errorsHandels.*;
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

import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
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
    void testGetItems_NotRestaurantFound() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1,"item 1","item 1 des",1200,500,menu1);
        List<ItemResource> result = new ArrayList<>();
        result.add(item.toItemResource());
        when(menuItemService.getItems(0,1,1))
                .thenThrow(new RestaurantNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/0/category/1/menus/1/item"))
                .andExpect(status().isNotFound())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetItems_NotCategoryFound() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1,"item 1","item 1 des",1200,500,menu1);
        List<ItemResource> result = new ArrayList<>();
        result.add(item.toItemResource());
        when(menuItemService.getItems(1,0,1))
                .thenThrow(new CategoryNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/0/menus/1/item"))
                .andExpect(status().isNotFound())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetItems_NotMenuFound() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1,"item 1","item 1 des",1200,500,menu1);
        List<ItemResource> result = new ArrayList<>();
        result.add(item.toItemResource());
        when(menuItemService.getItems(1,1,0))
                .thenThrow(new MenuNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/0/item"))
                .andExpect(status().isNotFound())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetItems_MenuNotInCategory() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1,"item 1","item 1 des",1200,500,menu1);
        List<ItemResource> result = new ArrayList<>();
        result.add(item.toItemResource());
        when(menuItemService.getItems(1,1,5))
                .thenThrow(new MenuNotInCategoryException(5));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/5/item"))
                .andExpect(status().isBadRequest())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser()
    void testGetItems_Execption() throws Exception {


        when(menuItemService.getItems(1,1,1))
                .thenThrow(new Exception("error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/1/item"))
                .andExpect(status().isBadRequest())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser()
    public void testGetItems_bulk_Empty() throws Exception {

        List<ItemResource> result = new ArrayList<>();
        Integer[] itemsIds =  {1, 2};


        when(menuItemService.getItemsbyIds(itemsIds))
                .thenReturn(result);

//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("itemsIds", Arrays.asList(itemsIds));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(itemsIds);


        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/item")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isOk())
        ;




    }
    @Test
    @WithMockUser()
    void testGetItems_bulk_NotEmpty() throws Exception{
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        List<ItemResource> result = new ArrayList<>();
        Integer[] itemsIds =  {1, 2};
        result.add(item.toItemResource());

        when(menuItemService.getItemsbyIds(itemsIds))
                .thenReturn(result);

//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("itemsIds", Arrays.asList(itemsIds));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(itemsIds);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/item")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items",hasSize(1)))
                .andExpect(jsonPath("$.data.items[0].*",hasSize(6)))
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
    void testGetItems_bulk_Execption() throws Exception {
        Integer[] itemsIds={};
        when(menuItemService.getItemsbyIds(itemsIds))
                .thenThrow(new Exception("error"));
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("itemsIds", Arrays.asList(itemsIds));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(requestBody);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/item")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser()
    void testGetItemDetail_NotFound() throws Exception {

        when(menuItemService.getItemsDetails(1,1,1,0))
                .thenThrow(new ItemNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/1/item/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetItemDetail_RestaurantNotFound() throws Exception {

        when(menuItemService.getItemsDetails(0,1,1,1))
                .thenThrow(new RestaurantNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/0/category/1/menus/1/item/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetItemDetail_CategoryNotFound() throws Exception {

        when(menuItemService.getItemsDetails(1,0,1,1))
                .thenThrow(new CategoryNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/0/menus/1/item/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetItemDetail_MenuNotFound() throws Exception {

        when(menuItemService.getItemsDetails(1,1,0,1))
                .thenThrow(new MenuNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/0/item/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @WithMockUser()
    void testGetItemDetail_ItemNotInMenu() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        ItemResource result = item.toItemResource();
        when(menuItemService.getItemsDetails(1,1,1,5))
                .thenThrow(new ItemNotInMenuException(5));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/1/item/5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }
    @Test
    @WithMockUser()
    void testGetItemDetail_Exception() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        ItemResource result = item.toItemResource();
        when(menuItemService.getItemsDetails(1,1,1,1))
                .thenThrow(new Exception("error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/1/item/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }
    @Test
    @WithMockUser()
    void testGetItemDetail_Found() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        ItemResource result = item.toItemResource();
        when(menuItemService.getItemsDetails(1,1,1,1))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/1/item/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.item.*", hasSize(6)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.item.id", equalTo(((int) result.getId()))))
                .andExpect(jsonPath("$.data.item.name", equalTo(result.getName())))
                .andExpect(jsonPath("$.data.item.description", equalTo(result.getDescription())))
                .andExpect(jsonPath("$.data.item.quantity", equalTo((double) result.getQuantity())))
                .andExpect(jsonPath("$.data.item.price", equalTo((double) result.getPrice())))
                .andExpect(jsonPath("$.data.item.menu", equalTo((int)result.getMenu())))
        ;
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
    @WithMockUser(roles = "RESTAURANT")
    public void testCreateItem_RestaurantNotFound() throws Exception{
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        when(menuItemService.createItem(0,1,1,item))
                .thenThrow(new RestaurantNotFoundException(0));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/0/category/1/menus/1/item").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isNotFound());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testCreateItem_dublicated() throws Exception{
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item1 = new ItemEntity(1, "item 1", "description",1200,500, menu);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        when(menuItemService.createItem(1,1,1,item))
                .thenThrow(new Exception("item already exists"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/1/menus/1/item").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testCreateItem_MenuNotInCategory() throws Exception{
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        when(menuItemService.createItem(1,1,5,item))
                .thenThrow(new MenuNotInCategoryException(5));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/1/menus/5/item").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT", username = "aaa", password = "123")
    public void testCreateItem_restarantNotOwner() throws Exception{
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1L,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);

        when(menuItemService.createItem(1,1,1,item))
                .thenThrow(new UserNotOwnerOfRestaurantException());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/1/menus/1/item").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isForbidden());

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
    @WithMockUser(roles = "RESTAURANT",username = "aa",password = "123")
    public void testUpdateItem_restauarentNotOwner() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        when(menuItemService.updateItem(1,1,1,1,item))
                .thenThrow(new UserNotOwnerOfRestaurantException());
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
    public void testUpdateItem_nullValue() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, null, "description",1200,500, menu);
        when(menuItemService.updateItem(1,1,1,1,item))
                .thenThrow(new Exception("name is required"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1/menus/1/item/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testUpdateItem_RestaurantNotFound() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "name 1", "description",1200,500, menu);
        when(menuItemService.updateItem(0,1,1,1,item))
                .thenThrow(new RestaurantNotFoundException(0));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/0/category/1/menus/1/item/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testUpdateItem_ItemNotInMenu() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "name 1", "description",1200,500, menu);
        when(menuItemService.updateItem(1,1,1,5,item))
                .thenThrow(new ItemNotInMenuException(5));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1/menus/1/item/5").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());
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

    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aaa",password = "123")
    public void testDeleteItem_restauarentNotOwner() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        doThrow(new UserNotOwnerOfRestaurantException()).when(menuItemService).deleteItem(1,1, 1,1);
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
    public void testDeleteItem_restauarentNotFound() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        doThrow(new RestaurantNotFoundException(0)).when(menuItemService).deleteItem(0,1, 1,1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/0/category/1/menus/1/item/1").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testDeleteItem_ItemNotInMenu() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        doThrow(new ItemNotInMenuException(5)).when(menuItemService).deleteItem(1,1, 1,5);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1/menus/1/item/5").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testDeleteItem_Execption() throws Exception{

        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
        ItemEntity item = new ItemEntity(1, "item 1", "description",1200,500, menu);
        doThrow(new Exception("1")).when(menuItemService).deleteItem(1,1, 1,5);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(item);
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1/menus/1/item/5").contentType(APPLICATION_JSON)
                        .content(requestJson).with(csrf()))
                .andExpect(status().isBadRequest());
    }

}
