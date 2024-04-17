package com.itep.restaurant_service.acceptance_tests.restaurant;

import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

public class ListRestaurantsStepsDefinition {
    private static final ParameterizedTypeReference<Object> RESPONSE_TYPE = new ParameterizedTypeReference<>() {
    };
    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;
    @Autowired
    public ListRestaurantsStepsDefinition(RestTemplateBuilder builder, RestaurantRepository restaurantRepository) {
        restTemplate = builder.build();
        this.restaurantRepository = restaurantRepository;
    }
    private ResponseEntity<Object> result;
    @Given("Available restaurants in database to test list available restaurants")
    public void availableRestaurants(DataTable table) {
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


    @When("I query available restaurants without filters")
    public void getAvailableRestaurantsWithoutFilters() {
        String uri = "http://localhost:8080/restaurants";
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        result = restTemplate.exchange(request, RESPONSE_TYPE);;
    }


    @Then("List all the {int} available restaurants is returned")
    public void getAvailableRestaurantsWithoutFilters_CheckResponse(int size) {
        Object body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body)
                .isInstanceOf(List.class);
        if (!((List<?>)body).isEmpty()) {
            assertThat(body)
                    .asList()
                    .hasSize(size)
                    .element(0)
                    .hasFieldOrProperty("name")
                    .hasFieldOrProperty("address")
                    .hasFieldOrProperty("location")
                    .hasFieldOrProperty("status")
                    .hasFieldOrProperty("food")
                    .hasFieldOrProperty("cuisine")
                    .extracting("status")
                    .matches(Predicate.isEqual("online"));

        }
    }
    @When("I query available restaurants with food type {word} and cuisine type {word}")
    public void getAvailableRestaurantsWithFilters(String food, String cuisine) {
        String uri = "http://localhost:8080/restaurants?food=%s&cuisine=%s".formatted(food,cuisine);
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        result = restTemplate.exchange(request, RESPONSE_TYPE);;
    }

    @Then("List all the {int} available restaurants is given with food type {word} and cuisine type {word} are returned")
    public void getAvailableRestaurantsWithFilters_CheckResponse(int size, String  food, String  cuisine) {
        Object body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body)
                .isInstanceOf(List.class);
        if (!((List<?>)body).isEmpty()) {
            assertThat(body)
                    .asList()
                    .hasSize(size)
                    .singleElement()
                    .hasFieldOrProperty("name")
                    .hasFieldOrProperty("address")
                    .hasFieldOrProperty("location")
                    .hasFieldOrProperty("status")
                    .hasFieldOrProperty("food")
                    .hasFieldOrProperty("cuisine")
                    .extracting("status", "food", "cuisine")
                    .containsExactly("online", food, cuisine);
        }
    }
    @When("I query available restaurants with food type {word}")
    public void getAvailableRestaurantsWithOnlyFoodFilter(String food) {
        String uri = "http://localhost:8080/restaurants?food=%s".formatted(food);
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        result = restTemplate.exchange(request, RESPONSE_TYPE);;
    }


    @Then("List all the {int} available restaurants is given with food type {word} are returned")
    public void getAvailableRestaurantsWithOnlyFoodFilter_CheckResponse(int size, String  food) {
        Object body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body)
                .isInstanceOf(List.class);
        if (!((List<?>)body).isEmpty()) {
            assertThat(body)
                    .asList()
                    .hasSize(size)
                    .element(0)
                    .hasFieldOrProperty("name")
                    .hasFieldOrProperty("address")
                    .hasFieldOrProperty("location")
                    .hasFieldOrProperty("status")
                    .hasFieldOrProperty("food")
                    .hasFieldOrProperty("cuisine")
                    .extracting("status", "food")
                    .containsExactly("online", food);
        }
    }
    @When("I query available restaurants with cuisine type {word}")
    public void getAvailableRestaurantsWithOnlyCuisineFilter(String cuisine) {
        String uri = "http://localhost:8080/restaurants?cuisine=%s".formatted(cuisine);
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        result = restTemplate.exchange(request, RESPONSE_TYPE);;
    }


    @Then("List all the {int} available restaurants is given with cuisine type {word} are returned")
    public void getAvailableRestaurantsWithOnlyCuisineFilter_CheckResponse(int size, String  cuisine) {
        Object body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body)
                .isInstanceOf(List.class);
        if (!((List<?>)body).isEmpty()) {
            assertThat(body)
                    .asList()
                    .hasSize(size)
                    .element(0)
                    .hasFieldOrProperty("name")
                    .hasFieldOrProperty("address")
                    .hasFieldOrProperty("location")
                    .hasFieldOrProperty("status")
                    .hasFieldOrProperty("food")
                    .hasFieldOrProperty("cuisine")
                    .extracting("status", "cuisine")
                    .containsExactly("online", cuisine);
        }
    }
}
