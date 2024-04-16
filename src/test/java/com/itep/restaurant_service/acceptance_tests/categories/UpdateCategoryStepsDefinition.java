package com.itep.restaurant_service.acceptance_tests.categories;

import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import io.cucumber.datatable.DataTable;
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
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateCategoryStepsDefinition {
    private static final ParameterizedTypeReference<Object> RESPONSE_TYPE = new ParameterizedTypeReference<>() {
    };
    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    public UpdateCategoryStepsDefinition(RestTemplateBuilder builder, RestaurantRepository restaurantRepository, CategoryRepository categoryRepository) {
        restTemplate = builder.build();
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
    }
    private ResponseEntity<Object> result;

    private List<RestaurantEntity> restaurants = new ArrayList<>();
    @Given("Restaurants in database to test update category")
    public void availableRestaurants(DataTable table) {
        List<List<Object>> rows = table.asLists(Object.class);
        for (List<Object> columns: rows) {
            // pass header
            if (rows.get(0) == columns) continue;
            restaurants.add(restaurantRepository.save(new RestaurantEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), columns.get(2).toString(),
                    columns.get(3).toString(), columns.get(4).toString(), columns.get(5).toString(),
                    columns.get(6).toString(), new UserEntity(columns.get(7).toString(), columns.get(8).toString()),
                    new ArrayList<>())));
        }
    }
    @Given("Categories in database to test update category")
    public void availableCategories(DataTable table) {
        List<List<Object>> rows = table.asLists(Object.class);
        for (List<Object> columns: rows) {
            // pass header
            if (rows.get(0) == columns) continue;
            categoryRepository.save(new CategoryEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), restaurants.get(0),
                    new ArrayList<>()));
        }
    }

    @When("category with ID {long} is updated by {word} user with password {word} to restaurant with ID {long} with category details <categoryDetails>:")
    public void putCategory(long categoryId, String user, String password, long restaurantId, AddCategoryStepsDefinition.CategoryEntry categoryEntry) {
        String uri = "http://localhost:8080/restaurants/"+restaurantId+"/category/"+categoryId;

        HttpHeaders headers = new HttpHeaders();
        String encoding = Base64.getEncoder().encodeToString((user + ":" + password).getBytes());
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AddCategoryStepsDefinition.CategoryEntry> requestEntity = new HttpEntity<>(categoryEntry, headers);
        try {
            result = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, Object.class, RESPONSE_TYPE);;
        }catch (HttpClientErrorException e){
            if (e.getStatusCode().value()==401) {
                result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else if (e.getStatusCode().value()==400) {
                result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponseBodyAs(Map.class));
            }else if (e.getStatusCode().value()==403) {
                result = ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getResponseBodyAs(Map.class));
            }else if (e.getStatusCode().value()==404) {
                result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getResponseBodyAs(Map.class));
            }else {
                throw e;
            }
        }
    }


    @Then("category updated successfully")
    public void categoryUpdatedSuccessfully_CheckResponse() {
        Object body = result.getBody();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body)
                .hasFieldOrProperty("message")
                .extracting("message")
                .matches(Predicate.isEqual("Category updated successfully"));
    }

    @Then("category can not be updated duo to missing values or category not belong to the restaurant")
    public void missingCategoryFields_CheckResponse() {
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
    }
    @Then("not authorized to update category")
    public void notAuthorizedToUpdateCategory_CheckResponse() {
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Then("can not update category because restaurant not found")
    public void canNotUpdateCategoryRestaurantNotFound_CheckResponse() {
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
