package com.itep.restaurant_service.component;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

@ExtendWith(SpringExtension.class)
public class AcceptOrderClientTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AcceptOrderClient acceptOrderClient;

    @Test
    void testAcceptOrder() {
        // Given
        String orderId = "123";
        Boolean status = true;
        acceptOrderClient.baseUri = "http://example.com/";
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
        acceptOrderClient.baseUri = "http://example.com/";
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
