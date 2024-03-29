package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.RestaurantResource;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.UserRepository;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.repositories.entities.UserEntity;
import com.itep.restaurant_service.services.RestaurantService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
    public List<RestaurantResource> getAvailableFilteredRestaurantsByFoodAndCuisine(String food, String cuisine) {
        return restaurantRepository.findByFoodAndCuisineAndStatus(food, cuisine, "online") .stream()
                .map(RestaurantEntity::toRestaurantResource)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantResource> getAvailableFilteredRestaurantsByFood(String food) {
        return restaurantRepository.findByFoodAndStatus(food, "online") .stream()
                .map(RestaurantEntity::toRestaurantResource)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantResource> getAvailableFilteredRestaurantsByCuisine(String cuisine) {
        return restaurantRepository.findByCuisineAndStatus(cuisine, "online") .stream()
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
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public Optional<RestaurantResource> getRestaurantDetails(long restaurantId) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(restaurantId);
        return restaurantEntity.map(RestaurantEntity::toRestaurantResource);
    }


    @Override
    public Optional<UserEntity> getRestaurantOwner(long restaurantId) {
        var restaurant = restaurantRepository.findOwnerById(restaurantId);
        return Optional.of(restaurant.get().getOwner());
    }

    @Override
    public UserDetails getRestaurantUserByUsername(String username) {
        if("admin".equals(username)){
            return User.withUsername("admin")
                    .password((new BCryptPasswordEncoder()).encode("admin"))
                    .roles("ADMIN")
                    .build();
        }else{
            try {
                UserEntity user = userRepository.findById(username).orElseThrow(() -> new Exception("User Not Found"));
                return User.withUsername(user.getUsername())
                        .password((new BCryptPasswordEncoder()).encode(user.getPassword()))
                        .roles("RESTAURANT")
                        .build();

            } catch (Exception e) {
                return null;
            }
        }


    }

    @Override
    public void setRestaurantStatus(long id, String status) {
        restaurantRepository.updateStatus(id, status);
    }
}
