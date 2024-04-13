//package com.itep.restaurant_service.acceptance_tests;
//
//import com.itep.restaurant_service.repositories.RestaurantRepository;
//import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
//import com.itep.restaurant_service.repositories.entities.UserEntity;
//import io.cucumber.datatable.DataTable;
//import io.cucumber.java.Before;
//import io.cucumber.java.DataTableType;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Predicate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class GetRestaurantDetailsStepsDefinition {
//    private static final ParameterizedTypeReference<Object> RESPONSE_TYPE = new ParameterizedTypeReference<>() {};
//    private final RestTemplate restTemplate;
//    private final RestaurantRepository restaurantRepository;
//    @Autowired
//    public GetRestaurantDetailsStepsDefinition(RestTemplateBuilder builder, RestaurantRepository restaurantRepository) {
//        restTemplate = builder.build();
//        this.restaurantRepository = restaurantRepository;
//    }
//    private ResponseEntity<Object> result;
//    @Given("Available restaurants in database to test get restaurant details")
//    public void haveBooksInTheStore(DataTable table) {
//        List<List<Object>> rows = table.asLists(Object.class);
//        for (List<Object> columns: rows) {
//            // pass header
//            if (rows.get(0) == columns) continue;
//            restaurantRepository.save(new RestaurantEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), columns.get(2).toString(),
//                    columns.get(3).toString(), columns.get(4).toString(), columns.get(5).toString(),
//                    columns.get(6).toString(), new UserEntity(columns.get(7).toString(), columns.get(8).toString()),
//                    new ArrayList<>()));
//        }
//    }
//
//    @When("I want to get restaurant details with restaurant ID {long}")
//    public void getRestaurantDetails(long restaurantId) {
//        String uri = "http://localhost:8080/restaurants/"+restaurantId;
//        try {
//            RequestEntity<Void> request = RequestEntity.get(uri).build();
//            result = restTemplate.exchange(request, RESPONSE_TYPE);;
//        }catch (HttpClientErrorException e){
//            if (e.getStatusCode().value()==404) {
//                result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getResponseBodyAs(Map.class));
//            }else {
//                throw e;
//            }
//        }
//    }
//
//
//    @Then("restaurant details are returned for the specific ID {long}")
//    public void restaurantDetailsAreFoundSuccessfully_CheckResponse(long restaurantId) {
//        Object body = result.getBody();
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(body)
//                .hasFieldOrProperty("id")
//                .hasFieldOrProperty("name")
//                .hasFieldOrProperty("address")
//                .hasFieldOrProperty("location")
//                .hasFieldOrProperty("food")
//                .hasFieldOrProperty("cuisine")
//                .extracting("id", "name")
//                .containsExactly(1, "r1");
//    }
//    @Then("can not get restaurant details because restaurant not found")
//    public void restaurantNotFound_CheckResponse() {
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        assertThat(result.getBody()).isNotNull();
//        assertThat(((Map<String, Object>)result.getBody()).get("message")).isNotNull().isEqualTo("Restaurant not found");
//    }
//}
