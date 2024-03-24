package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.impl.RestaurantServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class RestaurantController {
    private final RestaurantServiceImpl restaurantService;

    public RestaurantController(RestaurantServiceImpl restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public List<RestaurantResource> getRestaurants(){
        return restaurantService.getAvailableRestaurants();
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
    @PostMapping("/restaurants/{restaurantId}/status")
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
        var userRestaurant = restaurantService.getRestaurantByUsername(auth.getName());
        if (userRestaurant.isEmpty()){
            return new ResponseEntity<>(
                    Map.of("message", "Your restaurant information not found",
                            "status", 404),
                    HttpStatus.NOT_FOUND);

        }
        if(restaurantResource.get().id != userRestaurant.get().id){
            return new ResponseEntity<>(
                    Map.of("message", "You don't have the permission to update this restaurant",
                            "status", 400),
                    HttpStatus.BAD_REQUEST);
        }

        String status = body.get("status").toString();
        if("offline".equals(status)||"online".equals(status)) {
            restaurantService.setRestaurantStatus(restaurantId, status);
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
