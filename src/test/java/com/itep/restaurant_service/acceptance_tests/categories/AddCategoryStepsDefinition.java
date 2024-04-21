//package com.itep.restaurant_service.acceptance_tests.categories;
//
//import com.itep.restaurant_service.repositories.CategoryRepository;
//import com.itep.restaurant_service.repositories.RestaurantRepository;
//import com.itep.restaurant_service.repositories.entities.CategoryEntity;
//import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
//import com.itep.restaurant_service.repositories.entities.UserEntity;
//import io.cucumber.datatable.DataTable;
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
//public class AddCategoryStepsDefinition {
//    private static final ParameterizedTypeReference<Object> RESPONSE_TYPE = new ParameterizedTypeReference<>() {
//    };
//    private final RestTemplate restTemplate;
//    private final RestaurantRepository restaurantRepository;
//    @Autowired
//    public AddCategoryStepsDefinition(RestTemplateBuilder builder, RestaurantRepository restaurantRepository) {
//        restTemplate = builder.build();
//        this.restaurantRepository = restaurantRepository;
//    }
//    private ResponseEntity<Object> result;
//
//    @Given("Restaurants in database to test add category")
//    public void availableRestaurants(DataTable table) {
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
//    @When("new category is added by {word} user with password {word} to restaurant with ID {long} with category details <categoryDetails>:")
//    public void addCategory(String user, String password, long restaurantId, CategoryEntry categoryEntry) {
//        String uri = "http://localhost:8080/restaurants/"+restaurantId+"/category";
//
//        HttpHeaders headers = new HttpHeaders();
//        String encoding = Base64.getEncoder().encodeToString((user + ":" + password).getBytes());
//        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<CategoryEntry> requestEntity = new HttpEntity<>(categoryEntry, headers);
//        try {
//            result = restTemplate.postForEntity(uri, requestEntity, Object.class, RESPONSE_TYPE);;
//        }catch (HttpClientErrorException e){
//            if (e.getStatusCode().value()==401) {
//                result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            } else if (e.getStatusCode().value()==400) {
//                result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponseBodyAs(Map.class));
//            }else if (e.getStatusCode().value()==403) {
//                result = ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getResponseBodyAs(Map.class));
//            }else if (e.getStatusCode().value()==404) {
//                result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getResponseBodyAs(Map.class));
//            }else {
//                throw e;
//            }
//        }
//    }
//
//
//    @Then("category added successfully")
//    public void categoryAddedSuccessfully_CheckResponse() {
//        Object body = result.getBody();
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(body)
//                .hasFieldOrProperty("message")
//                .extracting("message")
//                .matches(Predicate.isEqual("Category created successfully"));
//    }
//
//    @Then("category can not be created duo to missing values")
//    public void missingCategoryFields_CheckResponse() {
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(result.getBody()).isNotNull();
//    }
//    @Then("not authorized to add category")
//    public void notAuthorizedToAddCategory_CheckResponse() {
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
//    }
//    @Then("can not add category because restaurant not found")
//    public void canNotAddCategoryRestaurantNotFound_CheckResponse() {
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @DataTableType
//    @SuppressWarnings("unused")
//    public CategoryEntry convert(Map<String, String> tableRow) {
//        return new CategoryEntry(
//                tableRow.get("name")
//        );
//    }
//    public record CategoryEntry(String name) {
//    }
//
//}
