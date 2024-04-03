package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.controllers.restaurant_utils.list_restaurants.ListRestaurantsHandler;
import com.itep.restaurant_service.controllers.restaurant_utils.list_restaurants.impls.ListRestaurantsWithOnlyCuisineParam;
import com.itep.restaurant_service.controllers.restaurant_utils.list_restaurants.impls.ListRestaurantsWithOnlyFoodParam;
import com.itep.restaurant_service.controllers.restaurant_utils.list_restaurants.impls.ListRestaurantsWithTwoParams;
import com.itep.restaurant_service.controllers.restaurant_utils.list_restaurants.impls.ListRestaurantsWithoutParams;
import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class RestaurantController {
    private final RestaurantServiceImpl restaurantService;

    public RestaurantController(RestaurantServiceImpl restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public ResponseEntity<Object> getRestaurants(@RequestParam(value = "food", required = false) String food, @RequestParam(value = "cuisine", required = false) String cuisine){
        ListRestaurantsHandler handlerChain = new ListRestaurantsWithoutParams(new ListRestaurantsWithTwoParams(new ListRestaurantsWithOnlyFoodParam(new ListRestaurantsWithOnlyCuisineParam(null))));
        Map<String, Object> filtersMap = new HashMap<>();
        Optional.ofNullable(food).ifPresent(v -> filtersMap.put("food", v));
        Optional.ofNullable(cuisine).ifPresent(v -> filtersMap.put("cuisine", v));
        return handlerChain.handleRequest(filtersMap, restaurantService);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/restaurants")
    public ResponseEntity<Object> createRestaurant(@RequestBody RestaurantEntity body)  {
        try {
            restaurantService.createRestaurant(body);
            return new ResponseEntity<>(
                    Map.of("message","Restaurant created successfully",
                            "status", 200)
                    , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("message",e.getMessage(),
                            "status", 400)
                    , HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<Object> getRestaurantDetails(@PathVariable("restaurantId") long restaurantId){
        var restaurantResource = restaurantService.getRestaurantDetails(restaurantId);
        return restaurantResource.<ResponseEntity<Object>>map(
                        resource -> new ResponseEntity<>(
                                resource, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        Map.of("message", "Restaurant not found",
                                "status", 404)
                        , HttpStatus.NOT_FOUND));

    }



    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping("/restaurants/{restaurantId}/status")

    public ResponseEntity<Object> setRestaurantStatus(@PathVariable("restaurantId") long restaurantId, @RequestBody Map<String, Object> body){
        var restaurantResource = restaurantService.getRestaurantDetails(restaurantId);
        if(restaurantResource.isEmpty()){
            return new ResponseEntity<>(
                    Map.of("message", "Restaurant not found",
                            "status", 404),
                    HttpStatus.NOT_FOUND);
        }
        // check if the updated restaurant belong to the user who wants to update
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var owner = restaurantService.getRestaurantOwner(restaurantId);
        if (!auth.getName().equals(owner.get().getUsername())){
            return new ResponseEntity<>(
                    Map.of("message", "You don't have the permission to update this restaurant",
                            "status", 403),
                    HttpStatus.FORBIDDEN);

        }

        if("offline".equals(body.get("status"))||"online".equals(body.get("status"))) {
            restaurantService.setRestaurantStatus(restaurantId, body.get("status").toString());
            return new ResponseEntity<>(
                    Map.of("message", "Restaurant status updated successfully",
                            "status", 200)
                    , HttpStatus.OK);
        }else{
            return new ResponseEntity<>(
                    Map.of("message", "Restaurant status must be either 'offline' or 'online' only",
                            "status", 400)
                    , HttpStatus.BAD_REQUEST);
        }

    }
}
