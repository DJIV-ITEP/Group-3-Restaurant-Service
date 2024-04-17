//package com.itep.restaurant_service.acceptance_tests.items;
//
//import com.itep.restaurant_service.repositories.CategoryRepository;
//import com.itep.restaurant_service.repositories.MenuItemRepository;
//import com.itep.restaurant_service.repositories.MenuRepository;
//import com.itep.restaurant_service.repositories.RestaurantRepository;
//import com.itep.restaurant_service.repositories.entities.*;
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
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class GetItemDetailsStepsDefinition {
//    private static final ParameterizedTypeReference<Object> RESPONSE_TYPE = new ParameterizedTypeReference<>() {
//    };
//    private final RestTemplate restTemplate;
//    private final RestaurantRepository restaurantRepository;
//    private final CategoryRepository categoryRepository;
//    private final MenuRepository menuRepository;
//    private final MenuItemRepository itemRepository;
//    @Autowired
//    public GetItemDetailsStepsDefinition(RestTemplateBuilder builder, RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, MenuRepository menuRepository, MenuItemRepository itemRepository) {
//        restTemplate = builder.build();
//        this.restaurantRepository = restaurantRepository;
//        this.categoryRepository = categoryRepository;
//        this.menuRepository = menuRepository;
//        this.itemRepository = itemRepository;
//    }
//    private ResponseEntity<Object> result;
//
//    private List<RestaurantEntity> restaurants = new ArrayList<>();
//    private List<CategoryEntity> categories = new ArrayList<>();
//    private List<MenuEntity> menus = new ArrayList<>();
//
//    @Given("Restaurants in database to test get item details")
//    public void availableRestaurants(DataTable table) {
//        List<List<Object>> rows = table.asLists(Object.class);
//        for (List<Object> columns: rows) {
//            // pass header
//            if (rows.get(0) == columns) continue;
//            restaurants.add(restaurantRepository.save(new RestaurantEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), columns.get(2).toString(),
//                    columns.get(3).toString(), columns.get(4).toString(), columns.get(5).toString(),
//                    columns.get(6).toString(), new UserEntity(columns.get(7).toString(), columns.get(8).toString()),
//                    new ArrayList<>())));
//        }
//    }
//    @Given("Categories in database to test get item details")
//    public void availableCategories(DataTable table) {
//        List<List<Object>> rows = table.asLists(Object.class);
//        for (List<Object> columns: rows) {
//            // pass header
//            if (rows.get(0) == columns) continue;
//            categories.add(categoryRepository.save(new CategoryEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), restaurants.get(0),
//                    new ArrayList<>())));
//        }
//    }
//
//    @Given("Menus in database to test get item details")
//    public void availableMenus(DataTable table) {
//        List<List<Object>> rows = table.asLists(Object.class);
//        for (List<Object> columns: rows) {
//            // pass header
//            if (rows.get(0) == columns) continue;
//            menus.add(menuRepository.save(new MenuEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), categories.get(0),
//                    new ArrayList<>())));
//        }
//
//    }
//    @Given("Items in database to test get item details")
//    public void availableItems(DataTable table) {
//        List<List<Object>> rows = table.asLists(Object.class);
//        for (List<Object> columns: rows) {
//            // pass header
//            if (rows.get(0) == columns) continue;
//            itemRepository.save(new ItemEntity(Integer.parseInt(columns.get(0).toString()), columns.get(1).toString(), columns.get(2).toString(), Double.parseDouble(columns.get(3).toString()), Double.parseDouble(columns.get(4).toString()), menus.get(0)));
//        }
//
//    }
//
//
//    @When("I query an item with ID {long} of menu with ID {long} of category with ID {long} restaurant with ID {long}")
//    public void getItem(long itemId, long menuId, long categoryId, long restaurantId) {
//        String uri = "http://localhost:8080/restaurants/"+restaurantId+"/category/"+categoryId+"/menus/"+menuId+"/item/"+itemId;
//        try{
//            RequestEntity<Void> request = RequestEntity.get(uri).build();
//            result = restTemplate.exchange(request, RESPONSE_TYPE);;
//        }catch (HttpClientErrorException e){
//            if (e.getStatusCode().value()==400){
//                result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponseBodyAs(Map.class));
//            }else if (e.getStatusCode().value()==404) {
//                result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getResponseBodyAs(Map.class));
//            }else {
//                throw e;
//            }
//        }
//    }
//
//
//    @Then("item detail with ID {int} is returned and its name is {word} and price {double}")
//    public void getItem_CheckResponse(int menuId, String name, double price) {
//        Object body = result.getBody();
//        assertThat(body).isNotNull();
//        assertThat(((Map<String, Map>) body).get("data").get("item"))
//                .hasFieldOrProperty("id")
//                .hasFieldOrProperty("name")
//                .hasFieldOrProperty("description")
//                .hasFieldOrProperty("quantity")
//                .hasFieldOrProperty("price").extracting("id", "name", "price")
//                .containsExactly(menuId, name, price);
//    }
//
//    @Then("can not get item details because category not found")
//    public void badRequest_CheckResponse() {
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(result.getBody()).isNotNull();
//    }
//    @Then("can not get item details because item not found or menu not found or restaurant not found")
//    public void restaurantNotFound_CheckResponse() {
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        assertThat(result.getBody()).isNotNull();
//    }
//}
