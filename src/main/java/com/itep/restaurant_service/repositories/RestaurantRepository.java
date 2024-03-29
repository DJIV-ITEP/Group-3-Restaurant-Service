package com.itep.restaurant_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itep.restaurant_service.repositories.entities.RestaurantEntity;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    List<RestaurantEntity> findByStatus(String status);

    List<RestaurantEntity> getAll();
}