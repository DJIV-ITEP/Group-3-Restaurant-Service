//package com.itep.restaurant_service.acceptance_tests.items;
//
//import com.itep.restaurant_service.repositories.CategoryRepository;
//import com.itep.restaurant_service.repositories.MenuRepository;
//import com.itep.restaurant_service.repositories.RestaurantRepository;
//import com.itep.restaurant_service.repositories.entities.CategoryEntity;
//import com.itep.restaurant_service.repositories.entities.MenuEntity;
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
//public class AddItemStepsDefinition {
//    private static final ParameterizedTypeReference<Object> RESPONSE_TYPE = new ParameterizedTypeReference<>() {
//    };
//    private final RestTemplate restTemplate;
//    private final RestaurantRepository restaurantRepository;
//    private final CategoryRepository categoryRepository;
//    private final MenuRepository menuRepository;
//    @Autowired
//    public AddItemStepsDefinition(RestTemplateBuilder builder, RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, MenuRepository menuRepository) {
//        restTemplate = builder.build();
//        this.restaurantRepository = restaurantRepository;
//        this.categoryRepository = categoryRepository;
//        this.menuRepository = menuRepository;
//    }
//    private ResponseEntity<Object> result;
//    private List<RestaurantEntity> restaurants = new ArrayList<>();
//
//    private List<CategoryEntity> categories = new ArrayList<>();
//
//    @Given("Restaurants in database to test add item")
//    public void availableRestaurants(DataTable table) {
//        List<List<Object>> rows = table.asLists(Object.class);
//        for (List<Object> columns: rows) {
//            // pass header
//            if (rows.get(0) == columns) continue;
//            restaurants.add( restaurantRepository.save(new RestaurantEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), columns.get(2).toString(),
//                    columns.get(3).toString(), columns.get(4).toString(), columns.get(5).toString(),
//                    columns.get(6).toString(), new UserEntity(columns.get(7).toString(), columns.get(8).toString()),
//                    new ArrayList<>())));
//        }
//    }
//    @Given("Categories in database to test add item")
//    public void availableCategories(DataTable table) {
//        List<List<Object>> rows = table.asLists(Object.class);
//        for (List<Object> columns: rows) {
//            // pass header
//            if (rows.get(0) == columns) continue;
//            categories.add(categoryRepository.save(new CategoryEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), restaurants.get(0),
//                    new ArrayList<>())));
//        }
//    }
//    @Given("Menus in database to test add item")
//    public void availableMenus(DataTable table) {
//        List<List<Object>> rows = table.asLists(Object.class);
//        for (List<Object> columns: rows) {
//            // pass header
//            if (rows.get(0) == columns) continue;
//            menuRepository.save(new MenuEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), categories.get(0),
//                    new ArrayList<>()));
//        }
//
//    }
//
//    @When("new item is added by {word} user with password {word} to restaurant with ID {long} and in category with ID {long} in menu with ID {long} with item details <itemDetails>:")
//    public void addItem(String user, String password, long restaurantId, long categoryId, long menuId, ItemEntry itemEntry) {
//        String uri = "http://localhost:8080/restaurants/"+restaurantId+"/category/"+categoryId+"/menus/"+menuId+"/item";
//
//        HttpHeaders headers = new HttpHeaders();
//        String encoding = Base64.getEncoder().encodeToString((user + ":" + password).getBytes());
//        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<ItemEntry> requestEntity = new HttpEntity<>(itemEntry, headers);
//        try {
//            result = restTemplate.postForEntity(uri, requestEntity, Object.class, RESPONSE_TYPE);;
//        }catch (HttpClientErrorException e){
//            if (e.getStatusCode().value()==401) {
//                result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            } else if (e.getStatusCode().value()==400) {
//                result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponseBodyAs(Map.class));
//            }else if (e.getStatusCode().value()==401) {
//                result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getResponseBodyAs(Map.class));
//            }else if (e.getStatusCode().value()==404) {
//                result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getResponseBodyAs(Map.class));
//            }else {
//                throw e;
//            }
//        }
//    }
//
//    @Then("item added successfully")
//    public void menuAddedSuccessfully_CheckResponse() {
//        Object body = result.getBody();
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(body)
//                .hasFieldOrProperty("message")
//                .extracting("message")
//                .matches(Predicate.isEqual("Menu Item created successfully"));
//    }
//
//    @Then("item can not be created duo to missing values")
//    public void missingMenuFields_CheckResponse() {
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(result.getBody()).isNotNull();
//    }
//    @Then("not authorized to add item")
//    public void notAuthorizedToAddMenu_CheckResponse() {
//        System.out.println(result.getBody());
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//    }
//    @Then("can not add item because restaurant not found")
//    public void canNotAddMenuRestaurantNotFound_CheckResponse() {
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @DataTableType
//    @SuppressWarnings("unused")
//    public ItemEntry convert(Map<String, String> tableRow) {
//        return new ItemEntry(
//                tableRow.get("name"),
//                Double.parseDouble(tableRow.get("price")),
//                tableRow.get("description"),
//                Double.parseDouble(tableRow.get("quantity"))
//        );
//    }
//    public record ItemEntry(String name, double price, String description, double quantity) {
//    }
//
//}
