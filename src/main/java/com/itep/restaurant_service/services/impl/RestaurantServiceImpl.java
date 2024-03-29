package com.itep.restaurant_service.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.AdminEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<RestaurantResource> getAvailableRestaurants() {
        return restaurantRepository.findByStatus("online").stream()
                .map(RestaurantEntity::toRestaurantResource)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantResource> getAllRestaurants() {
        return restaurantRepository.getAll().stream()
                .map(RestaurantEntity::toRestaurantResource)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResource createRestaurant(RestaurantEntity body) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        body.setAdded_by(new AdminEntity(auth.getName(), ""));
        try {
            return restaurantRepository.save(body).toRestaurantResource();
        } catch (Exception e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new Exception("restaurant with this username already exists");
            } else if (e.getMessage().contains("not-null property references a null")) {
                throw new Exception("You must provide all the restaurant fields");
            }
            throw new Exception("unknown error");
        }

    }

    @Override
    public Optional<RestaurantResource> getRestaurantDetails(long restaurantId) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(restaurantId);
        return restaurantEntity.map(RestaurantEntity::toRestaurantResource);
    }
}
