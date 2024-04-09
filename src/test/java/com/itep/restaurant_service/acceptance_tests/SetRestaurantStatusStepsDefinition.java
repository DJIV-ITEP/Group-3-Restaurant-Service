package com.itep.restaurant_service.acceptance_tests;

import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SetRestaurantStatusStepsDefinition {
    private static final ParameterizedTypeReference<Object> RESPONSE_TYPE = new ParameterizedTypeReference<>() {};
    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;
    @Autowired
    public SetRestaurantStatusStepsDefinition(RestTemplateBuilder builder, RestaurantRepository restaurantRepository) {
        restTemplate = builder.build();
        this.restaurantRepository = restaurantRepository;
    }
    private ResponseEntity<Object> result;
    @Given("Available restaurants in database to test set status")
    public void haveBooksInTheStore(DataTable table) {
        List<List<Object>> rows = table.asLists(Object.class);
        for (List<Object> columns: rows) {
            // pass header
            if (rows.get(0) == columns) continue;
            restaurantRepository.save(new RestaurantEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), columns.get(2).toString(),
                    columns.get(3).toString(), columns.get(4).toString(), columns.get(5).toString(),
                    columns.get(6).toString(), new UserEntity(columns.get(7).toString(), columns.get(8).toString()),
                    new ArrayList<>()));
        }
    }

    @When("User {word} with password {word} wants to set restaurant status to {word} for restaurant with ID {long}")
    public void setRestaurantStatus(String user, String password, String status, long restaurantId) {
        String uri = "http://localhost:8080/restaurants/"+restaurantId+"/status";
        HttpHeaders headers = new HttpHeaders();
        String encoding = Base64.getEncoder().encodeToString((user + ":" + password).getBytes());
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(Map.of("status", status), headers);
        try {
            result = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, Object.class, RESPONSE_TYPE);;
        }catch (HttpClientErrorException e){
            if (e.getStatusCode().value()==401) {
                result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else if (e.getStatusCode().value()==400) {
                result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponseBodyAs(Map.class));
            } else if (e.getStatusCode().value()==403) {
                result = ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getResponseBodyAs(Map.class));
            } else if (e.getStatusCode().value()==404) {
                result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getResponseBodyAs(Map.class));
            }else {
                throw e;
            }
        }
    }


    @Then("restaurant status set successfully")
    public void restaurantStatusSetSuccessfully_CheckResponse() {
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(((Map<String, Object>)result.getBody()).get("message")).isNotNull().isEqualTo("Restaurant status updated successfully");
    }
    @Then("restaurant status can not be set because the user is unauthorized")
    public void restaurantStatusNotSetUnauthorized_CheckResponse() {
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Then("restaurant status can not be set because the restaurant not found")
    public void restaurantStatusNotSetRestaurantNotFound_CheckResponse() {
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isNotNull();
        assertThat(((Map<String, Object>)result.getBody()).get("message")).isNotNull().isEqualTo("Restaurant not found");
    }
    @Then("restaurant status can not be set because the user does not own the restaurant")
    public void restaurantStatusNotSetUserNotTheOwner_CheckResponse() {
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(result.getBody()).isNotNull();
        assertThat(((Map<String, Object>)result.getBody()).get("message")).isNotNull().isEqualTo("You don't have the permission to update this restaurant");
    }
    @Then("restaurant status can not be set because the invalid status")
    public void restaurantStatusNotSetInvalidStatus_CheckResponse() {
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(((Map<String, Object>)result.getBody()).get("message")).isNotNull().isEqualTo("Restaurant status must be either 'offline' or 'online' only");
    }
}
