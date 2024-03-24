package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.UserRepository;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import com.itep.restaurant_service.services.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<RestaurantResource> getAvailableRestaurants() {
        return restaurantRepository.findByStatus("online") .stream()
                .map(RestaurantEntity::toRestaurantResource)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResource createRestaurant(RestaurantEntity body) throws Exception {
        try{
            userRepository.save(body.getOwner());
            return restaurantRepository.save(body).toRestaurantResource();
        }catch (Exception e){
            if(e.getMessage().contains("duplicate key value violates unique constraint")){
                throw new Exception("restaurant with this username already exists");
            }
            else if (e.getMessage().contains("not-null property references a null")) {
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

    @Override
    public Optional<UserEntity> getRestaurantOwner(long restaurantId) {
        return restaurantRepository.findOwnerById(restaurantId);
    }

    @Override
    public void setRestaurantStatus(long id, String status) {
        restaurantRepository.updateStatus(id, status);
    }
}
