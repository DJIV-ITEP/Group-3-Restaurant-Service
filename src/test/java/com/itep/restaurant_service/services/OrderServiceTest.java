package com.itep.restaurant_service.services;

import com.itep.restaurant_service.component.AcceptOrderClient;
import com.itep.restaurant_service.component.GetOrderClient;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.UserRepository;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import com.itep.restaurant_service.services.impl.OrderServiceImpl;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import com.itep.restaurant_service.services.impl.errorsHandels.RestaurantNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.UserNotOwnerOfRestaurantException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @Mock
    AcceptOrderClient acceptOrderClient;
    @Mock
    GetOrderClient getOrderClient;
    @Mock
    RestaurantRepository restaurantRepository;
    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testGetIncomingOrders_RestaurantNotFound() throws Exception {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());
        Throwable exception = assertThrows(Exception.class, () -> {
            orderService.getIncomingOrders(1L);
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }
    @Test
    @WithMockUser(username = "o", password = "o")
    void testGetIncomingOrders_NotOwner() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        Throwable exception = assertThrows(Exception.class, () -> {
            orderService.getIncomingOrders(1L);
        });

        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testGetIncomingOrders_Success() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        given(getOrderClient.getOrderList(1L))
                .willReturn(List.of());
        orderService.getIncomingOrders(1L);
    }





    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testMarkOrderAsReady_RestaurantNotFound() throws Exception {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());
        Throwable exception = assertThrows(Exception.class, () -> {
            orderService.markOrderAsReady(1L, "UID");
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }
    @Test
    @WithMockUser(username = "o", password = "o")
    void testMarkOrderAsReady_NotOwner() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        Throwable exception = assertThrows(Exception.class, () -> {
            orderService.markOrderAsReady(1L, "UID");
        });

        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testMarkOrderAsReady_Success() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        given(getOrderClient.getStatus())
                .willReturn(List.of());
        given(acceptOrderClient.changeOrderStatus("UID", 1L))
                .willReturn(ResponseEntity.ok(""));

        orderService.markOrderAsReady(1L, "UID");
    }



    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testCancelOrder_RestaurantNotFound() throws Exception {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());
        Throwable exception = assertThrows(Exception.class, () -> {
            orderService.cancelOrder(1L, "UID");
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }
    @Test
    @WithMockUser(username = "o", password = "o")
    void testCancelOrder_NotOwner() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        Throwable exception = assertThrows(Exception.class, () -> {
            orderService.cancelOrder(1L, "UID");
        });

        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testCancelOrder_Success() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        given(acceptOrderClient.acceptOrder("UID", false))
                .willReturn(ResponseEntity.ok(""));
        orderService.cancelOrder(1L, "UID");
    }

    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAcceptOrder_RestaurantNotFound() throws Exception {
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.empty());
        Throwable exception = assertThrows(Exception.class, () -> {
            orderService.accaptOrder(1L, "UID");
        });

        assertThat(exception)
                .isInstanceOf(RestaurantNotFoundException.class);
    }
    @Test
    @WithMockUser(username = "o", password = "o")
    void testAcceptOrder_NotOwner() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));
        Throwable exception = assertThrows(Exception.class, () -> {
            orderService.accaptOrder(1L, "UID");
        });

        assertThat(exception)
                .isInstanceOf(UserNotOwnerOfRestaurantException.class);
    }
    @Test
    @WithMockUser(username = "owner", password = "owner")
    void testAcceptOrder_Success() throws Exception {
        RestaurantEntity restaurant1 = new RestaurantEntity(1, "name", "address", "location", "status", "Seafood", "Yemeni",new UserEntity("owner", "owner"), null);
        given(restaurantRepository.findById(1L))
                .willReturn(Optional.of(restaurant1));

        given(acceptOrderClient.acceptOrder("UID", true))
                .willReturn(ResponseEntity.ok(""));
        orderService.accaptOrder(1L, "UID");
    }
}
