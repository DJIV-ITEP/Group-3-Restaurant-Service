package com.itep.restaurant_service.component;

import com.itep.restaurant_service.models.OrderResource;
import com.itep.restaurant_service.models.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component()
public class GetOrderClient {
    public String baseUri;
    private final RestTemplate restTemplate;

    public GetOrderClient(
            @Value("${external.api.orders.uri}") String baseUri,
            RestTemplate restTemplate) {
        this.baseUri = baseUri;
        this.restTemplate = restTemplate;
    }

    public List<OrderResource> getOrderList(long restaurantId) throws RestClientException {
        var uri = UriComponentsBuilder.fromUriString(baseUri)
                .pathSegment("rest-history")
                .toUriString();
        uri = uri +'/' + String.valueOf(restaurantId);
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        Map<String, Object> responseBody = response.getBody().get(0);

        return  (List<OrderResource>) responseBody.get("data");
    }


    public List<OrderStatus> getStatus() throws RestClientException {
        var uri = UriComponentsBuilder.fromUriString(baseUri)
                .pathSegment("status-list")
                .toUriString();
        ResponseEntity<List<OrderStatus>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderStatus>>() {}
        );

        return  response.getBody();
    }
}
