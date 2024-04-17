package com.itep.restaurant_service.component;

import com.itep.restaurant_service.controllers.RestaurantController;
import com.itep.restaurant_service.security.WebSecurityConfig;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(AcceptOrderClient.class)
@ExtendWith(SpringExtension.class)
@Import(WebSecurityConfig.class)
public class AcceptOrderClientTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AcceptOrderClient acceptOrderClient;

    @Test
    public void testAcceptOrder() {
        // Given
        String orderId = "123";
        Boolean status = true;
        String uri = "http://example.com/rest-change-status/123?accept=true";
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Accepted", HttpStatus.OK);

        // When
        when(restTemplate.exchange(eq(uri), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<String> response = acceptOrderClient.acceptOrder(orderId, status);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Accepted", response.getBody());
    }

    @Test
    public void testChangeOrderStatus() {
        // Given
        String orderId = "123";
        long statusId = 1;
        String uri = "http://example.com/rest-change-status/123";
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Status Changed", HttpStatus.OK);

        // When
        when(restTemplate.exchange(eq(uri), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<String> response = acceptOrderClient.changeOrderStatus(orderId, statusId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status Changed", response.getBody());
    }
}
