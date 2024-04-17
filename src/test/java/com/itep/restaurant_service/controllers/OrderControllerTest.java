package com.itep.restaurant_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.models.OrderItems;
import com.itep.restaurant_service.models.OrderResource;
import com.itep.restaurant_service.models.OrderStatus;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import com.itep.restaurant_service.security.WebSecurityConfig;
import com.itep.restaurant_service.services.impl.OrderServiceImpl;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import com.itep.restaurant_service.services.impl.errorsHandels.RestaurantNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.UserNotOwnerOfRestaurantException;
import io.cucumber.java.it.Ma;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OrderControllers.class)
@ExtendWith(SpringExtension.class)
@Import(WebSecurityConfig.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantServiceImpl restaurantService;

    @MockBean
    private OrderServiceImpl orderService;

    @Test
    @WithMockUser()
    public void testgetIncomingOrders_notuser() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        List<OrderResource> orders= new ArrayList<>();
//        when(orderService.getIncomingOrders(1)).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/incomingOrders")

                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
        ;


    }

    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aaa",password = "123")
    public void testgetIncomingOrders_restaurantNotOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        List<OrderResource> orders= new ArrayList<>();
//        when(orderService.getIncomingOrders(1)).thenReturn(orders);
        when(orderService.getIncomingOrders(1))
                .thenThrow(new UserNotOwnerOfRestaurantException());
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/incomingOrders")

                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
        ;


    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testgetIncomingOrders_restaurantNotFound() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        List<OrderResource> orders= new ArrayList<>();
//        when(orderService.getIncomingOrders(1)).thenReturn(orders);
        when(orderService.getIncomingOrders(0))
                .thenThrow(new RestaurantNotFoundException(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/0/incomingOrders")

                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
        ;


    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testgetIncomingOrders_Execption() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        List<OrderResource> orders= new ArrayList<>();
//        when(orderService.getIncomingOrders(1)).thenReturn(orders);
        when(orderService.getIncomingOrders(1))
                .thenThrow(new Exception("error"));
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/incomingOrders")

                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;


    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testgetIncomingOrders_empty() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        List<OrderResource> orders= new ArrayList<>();
        when(orderService.getIncomingOrders(1)).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/incomingOrders")

                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
        ;


    }

//    @Test
//    @WithMockUser(roles = "RESTAURANT")
//    public void testgetIncomingOrders_Notempty() throws Exception {
//        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
//        List<OrderResource> orders= new ArrayList<>();
//        List<OrderItems> items= new ArrayList<>();
//        OrderItems item_1 = new OrderItems(1, "Pizza", 2,2500,"with extra something",5000);
//        items.add(item_1);
//        OrderResource order1 = new OrderResource("62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7", 1, "rest 1", 1, "customer 1", 1, "driver 1",
//                1, "address 1", 1, new OrderStatus(1, "Submitted"),5000,2,items);
//        orders.add(order1);
//        when(orderService.getIncomingOrders(1)).thenReturn(orders);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/incomingOrders")
//
//                        .contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.data.items",hasSize(1)))
//                .andExpect(jsonPath("$.data.items[0].*", hasSize(14)))
//                .andExpect(jsonPath("$.data.items[0].OrderId", equalTo(orders.get(0).getOrderId())))
//                .andExpect(jsonPath("$.data.items[0].RestaurantID", equalTo((int) orders.get(0).getRestaurantId())))
//                .andExpect(jsonPath("$.data.items[0].RestaurantName", equalTo(orders.get(0).getRestaurantName())))
//                .andExpect(jsonPath("$.data.items[0].CustomerID", equalTo((int) orders.get(0).getCustomerId())))
//                .andExpect(jsonPath("$.data.items[0].CustomerName", equalTo(orders.get(0).getCustomerName())))
//                .andExpect(jsonPath("$.data.items[0].DriverID", equalTo((int) orders.get(0).getDriverId())))
//                .andExpect(jsonPath("$.data.items[0].DriverName", equalTo(orders.get(0).getDriverName())))
//                .andExpect(jsonPath("$.data.items[0].AddressID", equalTo((int) orders.get(0).getAddressId())))
//                .andExpect(jsonPath("$.data.items[0].AddressDesc", equalTo(orders.get(0).getAddressDesc())))
//                .andExpect(jsonPath("$.data.items[0].OrderStatusID", equalTo((int) orders.get(0).getOrderStatusID())))
//                .andExpect(jsonPath("$.data.items[0].OrderStatus", equalTo(orders.get(0).getOrderStatus())))
//                .andExpect(jsonPath("$.data.items[0].OrderDetails", equalTo(orders.get(0).getOrderDetails())))
//                .andExpect(jsonPath("$.data.items[0].Total", equalTo(orders.get(0).getTotal())))
//                .andExpect(jsonPath("$.data.items[0].ItemCount", equalTo(orders.get(0).getItemCount())))
//                .andExpect(status().isOk())
//        ;
//
//
//    }

    @Test
    @WithMockUser()
    public void testSetAccapt_notuser() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);


        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/accapt")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testSetAccapt_restaurantOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        ResponseEntity<String> res = new ResponseEntity<>( "",HttpStatus.OK);
        when(orderService.accaptOrder(1,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/accapt")
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aaa",password = "123")
    public void testSetAccapt_restaurantNotOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        when(orderService.accaptOrder(1,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenThrow(new UserNotOwnerOfRestaurantException());

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/accapt")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testSetAccapt_restaurantNotFound() throws Exception {

        when(orderService.accaptOrder(0,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenThrow(new RestaurantNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/0/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/accapt")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testSetAccapt_Execption() throws Exception {

        when(orderService.accaptOrder(1,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenThrow(new Exception("error"));

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/accapt")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser()
    public void testSetReject_notuser() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);


        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/reject")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testSetReject_restaurantOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        ResponseEntity<String> res = new ResponseEntity<>( "",HttpStatus.OK);
        when(orderService.cancelOrder(1,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenReturn(res);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/reject")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aaa",password = "123")
    public void testSetRejact_restaurantNotOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        when(orderService.cancelOrder(1,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenThrow(new UserNotOwnerOfRestaurantException());

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/reject")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testSetRejact_restaurantNotFound() throws Exception {

        when(orderService.cancelOrder(0,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenThrow(new RestaurantNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/0/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/reject")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testSetRejact_Execption() throws Exception {

        when(orderService.cancelOrder(1,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenThrow(new Exception("error"));

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/reject")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser()
    public void testSetReady_notuser() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);


        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/markReady")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testSetReady_restaurantOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        List<OrderStatus> result = new ArrayList<>();
        result.add(new OrderStatus(4,"Submitted"));
        result.add(new OrderStatus(5,"Received"));


        ResponseEntity<String> res = new ResponseEntity<>( "",HttpStatus.OK);
        when(orderService.markOrderAsReady(1,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenReturn(res);
        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/markReady")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT",username = "aaa",password = "123")
    public void testSetReady_restaurantNotOwner() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity(1, "name", "address", "location", "status", "food", "cuisine", new UserEntity("rest1", "123"), null);
        when(orderService.markOrderAsReady(1,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenThrow(new UserNotOwnerOfRestaurantException());

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/markReady")
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testSetReady_restaurantNotFound() throws Exception {

        when(orderService.markOrderAsReady(0,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenThrow(new RestaurantNotFoundException(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/0/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/markReady")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testSetReady_Execption() throws Exception {

        when(orderService.markOrderAsReady(1,"62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7"))
                .thenThrow(new Exception("error"));

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1/orders/62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7/markReady")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}
