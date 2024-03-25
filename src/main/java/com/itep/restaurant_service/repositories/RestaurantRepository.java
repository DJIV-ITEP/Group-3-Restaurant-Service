package com.itep.restaurant_service.repositories;

import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    List<RestaurantEntity> findByStatus(String status);
    List<RestaurantEntity> findByFoodAndCuisineAndStatus(String food, String cuisine, String status);
    List<RestaurantEntity> findByFoodAndStatus(String food, String status);
    List<RestaurantEntity> findByCuisineAndStatus(String cuisine, String status);

    Optional<RestaurantEntity> findByUsername(String username);

    @Modifying
    @Query("update restaurants restaurant set restaurant.status = :status where restaurant.id = :id")
    void updateStatus(@Param(value = "id") long id, @Param(value = "status") String status);
}