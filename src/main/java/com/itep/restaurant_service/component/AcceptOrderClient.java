package com.itep.restaurant_service.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component()
public class AcceptOrderClient {
    public String baseUri;
    private final RestTemplate restTemplate;



    public AcceptOrderClient(
            @Value("${external.api.orders.uri}") String baseUri,
            RestTemplate restTemplate) {
        this.baseUri = baseUri;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> acceptOrder(String orderId, Boolean status) throws RestClientException {

        var uri = UriComponentsBuilder.fromUriString(baseUri)
                .pathSegment("rest-change-status" )
                .toUriString();
        uri = uri +'/'+ orderId+"?accept="+status.toString();
        Map<String, Object> body = Map.of("id", orderId, "accept", false);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body,headers);


        return restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);

    }

    public ResponseEntity<String> changeOrderStatus(String orderId, long statusId) throws RestClientException {
        var uri = UriComponentsBuilder.fromUriString(baseUri)
                .pathSegment("rest-change-status")
                .toUriString();
        uri = uri +'/' +orderId;
        Map<String, Object> body = Map.of("StatusId", String.valueOf(statusId));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body,headers);

        return restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);



    }

}
