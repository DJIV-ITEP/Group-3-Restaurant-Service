//package com.itep.restaurant_service.acceptance_tests;
//
//import io.cucumber.java.DataTableType;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Base64;
//import java.util.Map;
//import java.util.function.Predicate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class CreateRestaurantStepsDefinition {
//    private static final ParameterizedTypeReference<Object> RESPONSE_TYPE = new ParameterizedTypeReference<>() {};
//    private final RestTemplate restTemplate;
//    @Autowired
//    public CreateRestaurantStepsDefinition(RestTemplateBuilder builder) {
//        restTemplate = builder.build();
//    }
//    private ResponseEntity<Object> result;
//
//    @When("new restaurant is added by {word} user with password {word} with restaurant details <restaurantDetails>:")
//    public void adminCreateNewRestaurant(String user, String password, RestaurantEntry restaurantDetails) {
//        String uri = "http://localhost:8080/restaurants";
//        HttpHeaders headers = new HttpHeaders();
//        String encoding = Base64.getEncoder().encodeToString((user + ":" + password).getBytes());
//        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<RestaurantEntry> requestEntity = new HttpEntity<>(restaurantDetails, headers);
//        try {
//            result = restTemplate.postForEntity(uri, requestEntity, Object.class, RESPONSE_TYPE);;
//        }catch (HttpClientErrorException e){
//            if (e.getStatusCode().value()==401) {
//                result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            } else if (e.getStatusCode().value()==400) {
//                result = ResponseEntity.badRequest().body(e.getResponseBodyAs(Map.class));
//            }else {
//                throw e;
//            }
//        }
//    }
//
//
//    @Then("restaurant created successfully")
//    public void restaurantCreatedSuccessfully_CheckResponse() {
//        Object body = result.getBody();
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(body)
//                .hasFieldOrProperty("message")
//                .extracting("message")
//                .matches(Predicate.isEqual("Restaurant created successfully"));
//    }
//    @Then("restaurant can not be created duo to missing values")
//    public void missingRestaurantFields_CheckResponse() {
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(result.getBody()).isNotNull();
//        assertThat(((Map<String, Object>)result.getBody()).get("message")).isNotNull().isEqualTo("You must provide all the restaurant fields");
//    }
//    @Then("not authorized to add restaurant")
//    public void notAuthorizedToAddRestaurant_CheckResponse() {
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//    }
//    @DataTableType
//    @SuppressWarnings("unused")
//    public RestaurantEntry convert(Map<String, String> tableRow) {
//        return new RestaurantEntry(
//                tableRow.get("name"),
//                tableRow.get("address"),
//                tableRow.get("location"),
//                tableRow.get("status"),
//                tableRow.get("food"),
//                tableRow.get("cuisine"),
//                new OwnerEntry(tableRow.get("owner.username"), tableRow.get("owner.password"))
//        );
//    }
//    public record RestaurantEntry(String name, String address, String location, String status, String food, String cuisine, OwnerEntry owner) {
//    }
//    public record OwnerEntry(String username, String password){}
//}
