package com.itep.restaurant_service.repositories;

import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    List<RestaurantEntity> findByStatus(String status);
    List<RestaurantEntity> findByFoodAndCuisineAndStatus(String food, String cuisine, String status);
    List<RestaurantEntity> findByFoodAndStatus(String food, String status);
    List<RestaurantEntity> findByCuisineAndStatus(String cuisine, String status);
}