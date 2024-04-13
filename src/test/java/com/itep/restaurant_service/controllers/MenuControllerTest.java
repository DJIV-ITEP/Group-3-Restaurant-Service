//package com.itep.restaurant_service.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.itep.restaurant_service.models.CategoryResource;
//import com.itep.restaurant_service.models.MenuResource;
//import com.itep.restaurant_service.repositories.MenuRepository;
//import com.itep.restaurant_service.repositories.UserRepository;
//import com.itep.restaurant_service.repositories.entities.CategoryEntity;
//import com.itep.restaurant_service.repositories.entities.MenuEntity;
//import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
//import com.itep.restaurant_service.repositories.entities.UserEntity;
//import com.itep.restaurant_service.security.WebSecurityConfig;
//import com.itep.restaurant_service.services.impl.MenuServiceImpl;
//import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
//import com.itep.restaurant_service.services.impl.errorsHandels.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(MenuController.class)
//@ExtendWith(SpringExtension.class)
//@Import(WebSecurityConfig.class)
//public class MenuControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    MenuRepository menuRepository;
//    @MockBean
//    UserRepository userRepository;
//    @MockBean
//    private RestaurantServiceImpl restaurantService;
//    @MockBean
//    private MenuServiceImpl menuService;
//
//
//    @Test
//    @WithMockUser()
//    void testGetMenu_Empty() throws Exception {
//        List<MenuResource> result = new ArrayList<>();
//        when(menuService.getMenues(1,1))
//                .thenReturn(result);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    @WithMockUser()
//    void testGetMenu_NotEmpty() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1L, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
//        CategoryEntity category = new CategoryEntity(1L,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1L,"menu 1",category,null);
//
//        List<MenuResource> result = new ArrayList<>();
//        result.add(menu1.toMenuResource());
//
//        when(menuService.getMenues(1L,1L))
//                .thenReturn(result);
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.items",hasSize(1)))
//                .andExpect(jsonPath("$.data.items[0].*",hasSize(3)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.data.items[0].id", equalTo(((int) result.get(0).getId()))))
//                .andExpect(jsonPath("$.data.items[0].name", equalTo(result.get(0).getName())))
//                .andExpect(jsonPath("$.data.items[0].category", equalTo((int) result.get(0).getCategory())))
//
//                ;
//    }
//    @Test
//    @WithMockUser()
//    void testGetMenu_NotRestaurantFound() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1L, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
//        CategoryEntity category = new CategoryEntity(1L,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1L,"menu 1",category,null);
//
//        List<MenuResource> result = new ArrayList<>();
//        result.add(menu1.toMenuResource());
//        when(menuService.getMenues(0,1))
//                .thenThrow(new RestaurantNotFoundException(0));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/0/category/1/menus"))
//                .andExpect(status().isNotFound())
//
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//    @Test
//    @WithMockUser()
//    void testGetMenu_NotCategoryFound() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1L, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
//        CategoryEntity category = new CategoryEntity(1L,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1L,"menu 1",category,null);
//
//        List<MenuResource> result = new ArrayList<>();
//        result.add(menu1.toMenuResource());
//        when(menuService.getMenues(1,0))
//                .thenThrow(new CategoryNotFoundException(0));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/0/menus"))
//                .andExpect(status().isNotFound())
//
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//    @Test
//    @WithMockUser()
//    void testGetMenu_CategoryNotInRestaurant() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1L, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
//        CategoryEntity category = new CategoryEntity(1L,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1L,"menu 1",category,null);
//
//        List<MenuResource> result = new ArrayList<>();
//        result.add(menu1.toMenuResource());
//        when(menuService.getMenues(1,5))
//                .thenThrow(new CategoryNotInRestaurantException(1));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/5/menus"))
//                .andExpect(status().isBadRequest())
//
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//    @Test
//    @WithMockUser()
//    void testGetMenuDetail_NotFound() throws Exception {
//
//        when(menuService.getMenueDetails(1,1,0))
//                .thenThrow(new MenuNotFoundException(0));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/0"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//    @Test
//    @WithMockUser()
//    void testGetMenuDetail_NotInCategory() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1L, "name", "address", "location", "status", "food", "cuisine", new UserEntity("owner", "owner"), null);
//        CategoryEntity category = new CategoryEntity(1L,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1L,"menu 1",null,null);
//        when(menuService.getMenueDetails(1,1,5))
//                .thenThrow(new MenuNotInCategoryException(5));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/5"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//    @Test
//    @WithMockUser()
//    void testGetMenuDetail_NotRestaurentFound() throws Exception {
//
//        when(menuService.getMenueDetails(0,1,1))
//                .thenThrow(new RestaurantNotFoundException(0));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/0/category/1/menus/1"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//    @Test
//    @WithMockUser()
//    void testGetMenuDetail_NotCategoryFound() throws Exception {
//
//        when(menuService.getMenueDetails(1,0,1))
//                .thenThrow(new CategoryNotFoundException(0));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/0/menus/1"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//    @Test
//    @WithMockUser()
//    void testGetMenuDetail_Found() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//
//        MenuResource result = new MenuResource(1, "menu 1", category.getId());
//        when(menuService.getMenueDetails(1,1,1))
//                .thenReturn(result);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/category/1/menus/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.item.*", hasSize(3)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.data.item.id", equalTo(((int) result.getId()))))
//                .andExpect(jsonPath("$.data.item.name", equalTo(result.getName())))
//                .andExpect(jsonPath("$.data.item.category", equalTo((int) result.getCategory())))
//        ;
//    }
//    @Test
//    @WithMockUser()
//    public void testCreateMenu_noUser() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1L,"menu 1",category,null);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/1/menus").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isForbidden());
//    }
//
//
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testCreateMenu_restaurantOwner() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/1/menus").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(roles = "RESTAURANT",username = "aa",password = "123")
//    public void testCreateMenu_restaurantNotOwner() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        when(menuService.createMenu(1,1,menu1))
//                .thenThrow(new UserNotOwnerOfRestaurantException());
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/1/menus").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testCreateMenu_restaurantNotFound() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", null,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        when(menuService.createMenu(0,1,menu1))
//                .thenThrow(new RestaurantNotFoundException(0));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/0/category/1/menus").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testCreateMenu_CategoryNotFound() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",null,null);
//        when(menuService.createMenu(1,0,menu1))
//                .thenThrow(new CategoryNotFoundException(0));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/0/menus").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testCreateMenu_CategoryNotInRestaurant() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        when(menuService.createMenu(1,5,menu1))
//                .thenThrow(new CategoryNotInRestaurantException(5));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/5/menus").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isBadRequest());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testCreateMenu_MissingValue() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,null,category,null);
//        when(menuService.createMenu(1,1,menu1))
//                .thenThrow(new Exception("You must provide all the menu fields"));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/1/menus").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isBadRequest());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testCreateMenu_alreadyExist() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu = new MenuEntity(1,"menu 1",category,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        when(menuService.createMenu(1,1,menu1))
//                .thenThrow(new Exception("menu with this name already exists in category"));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/category/1/menus").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @WithMockUser()
//    public void testUpdateMenu_notuser() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1L,"menu 1",category,null);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isForbidden());
//
//    }
//
//
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testUpdateMenu_restaurantOwner() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isOk());
//
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT",username = "aa",password = "123")
//    public void testUpdateMenu_restaurantNotOwner() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        when(menuService.updateMenu(1,1,1,menu1))
//                .thenThrow(new UserNotOwnerOfRestaurantException());
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testUpdateMenu_restaurantNotFound() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", null,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        when(menuService.updateMenu(0,1,1,menu1))
//                .thenThrow(new RestaurantNotFoundException(0));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/0/category/1/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testUpdateMenu_CategoryNotFound() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",null,null);
//        when(menuService.updateMenu(1,0,1,menu1))
//                .thenThrow(new CategoryNotFoundException(0));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/0/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testUpdateMenu_CategoryNotInRestaurant() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        when(menuService.updateMenu(1,5,1,menu1))
//                .thenThrow(new CategoryNotInRestaurantException(5));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/5/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isBadRequest());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testUpdateMenu_MissingValue() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,null,category,null);
//        when(menuService.updateMenu(1,1,1,menu1))
//                .thenThrow(new Exception("You must provide all the menu fields"));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/category/1/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isBadRequest());
//    }
//
//
//
//
//    @Test
//    @WithMockUser()
//    public void testDeleteMenu_notuser() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isForbidden());
//
//    }
//
//
//
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testDeleteMenu_restaurantOwner() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isOk());
//
//    }
//
//
//    @Test
//    @WithMockUser(roles = "RESTAURANT",username = "aa",password = "123")
//    public void testDeleteMenu_restaurantNotOwner() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        doThrow(new UserNotOwnerOfRestaurantException()).when(menuService).deleteMenu(1, 1,1);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testDeleteMenu_restaurantNotFound() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", null,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        doThrow(new RestaurantNotFoundException(1)).when(menuService).deleteMenu(0, 1,1);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/0/category/1/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testDeleteMenu_CategoryNotFound() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",null,null);
//        doThrow(new CategoryNotFoundException(1)).when(menuService).deleteMenu(1, 0,1);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/0/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testDeleteMenu_CategoryNotInRestaurant() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        doThrow(new CategoryNotInRestaurantException(1)).when(menuService).deleteMenu(1, 5,1);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/5/menus/1").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isBadRequest());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testDeleteMenu_MenuNotInCategory() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        doThrow(new MenuNotInCategoryException(1)).when(menuService).deleteMenu(1, 1,5);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1/menus/5").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isBadRequest());
//    }
//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testDeleteMenu_GeneralException() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        CategoryEntity category = new CategoryEntity(1,"name", restaurant,null);
//        MenuEntity menu1 = new MenuEntity(1,"menu 1",category,null);
//        doThrow(new Exception("1")).when(menuService).deleteMenu(1, 1,5);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=ow.writeValueAsString(menu1);
//        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1/category/1/menus/5").contentType(APPLICATION_JSON)
//                        .content(requestJson).with(csrf()))
//                .andExpect(status().isBadRequest());
//    }
//
//}
