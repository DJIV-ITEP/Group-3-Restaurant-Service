package com.itep.restaurant_service.repositories;

import com.itep.restaurant_service.repositories.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<ItemEntity, Long> {
}
