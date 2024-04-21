//package com.itep.restaurant_service.acceptance_tests.categories;
//
//import com.itep.restaurant_service.repositories.CategoryRepository;
//import com.itep.restaurant_service.repositories.RestaurantRepository;
//import com.itep.restaurant_service.repositories.entities.CategoryEntity;
//import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
//import com.itep.restaurant_service.repositories.entities.UserEntity;
//import io.cucumber.datatable.DataTable;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.RequestEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Predicate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class ListCategoriesStepsDefinition {
//    private static final ParameterizedTypeReference<Object> RESPONSE_TYPE = new ParameterizedTypeReference<>() {
//    };
//    private final RestTemplate restTemplate;
//    private final RestaurantRepository restaurantRepository;
//    private final CategoryRepository categoryRepository;
//    @Autowired
//    public ListCategoriesStepsDefinition(RestTemplateBuilder builder, RestaurantRepository restaurantRepository, CategoryRepository categoryRepository) {
//        restTemplate = builder.build();
//        this.restaurantRepository = restaurantRepository;
//        this.categoryRepository = categoryRepository;
//    }
//    private ResponseEntity<Object> result;
//
//    private RestaurantEntity restaurant;
//    @Given("Restaurants in database to test list categories")
//    public void availableRestaurants(DataTable table) {
//        List<List<Object>> rows = table.asLists(Object.class);
//        for (List<Object> columns: rows) {
//            // pass header
//            if (rows.get(0) == columns) continue;
//            restaurant = restaurantRepository.save(new RestaurantEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), columns.get(2).toString(),
//                    columns.get(3).toString(), columns.get(4).toString(), columns.get(5).toString(),
//                    columns.get(6).toString(), new UserEntity(columns.get(7).toString(), columns.get(8).toString()),
//                    new ArrayList<>()));
//        }
//    }
//    @Given("Categories in database to test list categories")
//    public void availableCategories(DataTable table) {
//        List<List<Object>> rows = table.asLists(Object.class);
//        for (List<Object> columns: rows) {
//            // pass header
//            if (rows.get(0) == columns) continue;
//            categoryRepository.save(new CategoryEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), restaurant,
//                    new ArrayList<>()));
//        }
//    }
//
//
//    @When("I query a list of categories of restaurant with ID {long}")
//    public void getCategories(long restaurantId) {
//        String uri = "http://localhost:8080/restaurants/"+restaurantId+"/category";
//        try{
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
//    @Then("List all the {int} available categories is returned")
//    public void getAvailableCategories_CheckResponse(int size) {
//        Object body = result.getBody();
//        assertThat(body).isNotNull();
//        assertThat(((Map<String, Map>) body).get("data").get("items"))
//                .isInstanceOf(List.class);
//        if (!((List<?>)((Map<String, Map>) body).get("data").get("items")).isEmpty()) {
//            assertThat(((Map<String, Map>) body).get("data").get("items"))
//                    .asList()
//                    .hasSize(size)
//                    .element(0)
//                    .hasFieldOrProperty("id")
//                    .hasFieldOrProperty("name");
//
//        }
//    }
//
//    @Then("can not get categories because restaurant not found")
//    public void restaurantNotFound_CheckResponse() {
//        Object body = result.getBody();
//        assertThat(body).isNotNull();
//
//    }
//
//}
