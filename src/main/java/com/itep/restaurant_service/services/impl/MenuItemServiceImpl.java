package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.MenuItemRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.services.MenuItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<ItemResource> getAllItems(Long menu_id) {
        return menuItemRepository.findByMenuId(menu_id).stream()
                .map(ItemEntity::toItemResource)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResource createItem(Long menu_id, ItemEntity body) throws Exception {
        try{
            Optional<MenuEntity> yourEntityOptional = menuRepository.findById(menu_id);
            if (yourEntityOptional.isPresent()) {
                MenuEntity yourEntity = yourEntityOptional.get();
                body.setMenu(yourEntity);

                return menuItemRepository.save(body).toItemResource();
            }
            else{
                throw new EntityNotFoundException("Category with id " + menu_id + " not found");
            }

        }
        catch (Exception e){
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
    public ItemResource updateItem(Long id, ItemEntity body) throws Exception {
        ItemEntity existItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new Exception("item not found"));
        existItem.setName(body.getName());
        existItem.setDescription(body.getDescription());
        existItem.setPrice(body.getPrice());
        try {
            return menuItemRepository.save(existItem).toItemResource();
        } catch (Exception e) {
            throw new Exception("Failed to update item");
        }
    }

    @Override
    public void deleteItem(Long id) throws Exception {
        try{
            menuItemRepository.deleteById(id);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
