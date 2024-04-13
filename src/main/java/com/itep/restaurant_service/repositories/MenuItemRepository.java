package com.itep.restaurant_service.repositories;

import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MenuItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findByMenuId(long menuId);
}
