//package com.itep.restaurant_service.component;
//
//import com.itep.restaurant_service.models.OrderItems;
//import com.itep.restaurant_service.models.OrderResource;
//import com.itep.restaurant_service.models.OrderStatus;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class GetOrderClientTest {
//    @Mock
//    private RestTemplate restTemplate;
//
//    @InjectMocks
//    private GetOrderClient getOrderClient;
//
//    @Test
//    public void testGetOrderList() {
//        // Mock response data
//        List<OrderResource> orderResources = Arrays.asList(
//
//                new OrderResource("62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7", 1, "rest 1", 1, "customer 1", 1, "driver 1",
//                1, "address 1", 1, new OrderStatus(1, "Submitted"),5000,2, Arrays.asList(new OrderItems(1,"product",2,2500,"any",5000))),
//                new OrderResource("62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f77", 2, "rest 2", 2, "customer 2", 2, "driver 2",
//                        2, "address 2", 2, new OrderStatus(2, "Submitted 2"),50000,4, Arrays.asList(new OrderItems(1,"product",4,10000,"any",50000)))
//        );
//        ResponseEntity<List<Map<String, Object>>> responseEntity =
//                new ResponseEntity<>(Collections.singletonList(Collections.singletonMap("data", orderResources)), HttpStatus.OK);
//
//        // Mock RestTemplate behavior
//        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(),
//                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
//
//        // Call the method under test
//        List<OrderResource> result = getOrderClient.getOrderList(1L);
//
//        // Verify the result
//        assertEquals(2, result.size());
//        assertEquals("62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f7", result.get(0).getOrderId());
//        assertEquals("62a8348e-e2e6-4b6d-b9ef-8bfdbb5ff2f77", result.get(1).getOrderId());
//    }
//
//    @Test
//    public void testGetStatus() {
//        // Mock response data
//        List<OrderStatus> orderStatuses = Arrays.asList(
//                new OrderStatus(1L, "Status1"),
//                new OrderStatus(2L, "Status2")
//        );
//        ResponseEntity<List<OrderStatus>> responseEntity =
//                new ResponseEntity<>(orderStatuses, HttpStatus.OK);
//
//        // Mock RestTemplate behavior
//        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(),
//                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
//
//        // Call the method under test
//        List<OrderStatus> result = getOrderClient.getStatus();
//
//        // Verify the result
//        assertEquals(2, result.size());
//        assertEquals("Status1", result.get(0).getName());
//        assertEquals("Status2", result.get(1).getName());
//    }
//}
