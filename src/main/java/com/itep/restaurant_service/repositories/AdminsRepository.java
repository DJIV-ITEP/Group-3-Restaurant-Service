package com.itep.restaurant_service.repositories;

import com.itep.restaurant_service.repositories.entities.AdminEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminsRepository extends JpaRepository<AdminEntity, Long> {
}