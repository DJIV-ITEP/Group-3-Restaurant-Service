package com.itep.restaurant_service.repositories;


import com.itep.restaurant_service.repositories.entities.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
}