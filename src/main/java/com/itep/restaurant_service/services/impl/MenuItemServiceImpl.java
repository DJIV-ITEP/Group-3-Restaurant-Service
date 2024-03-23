package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.repositories.MenuItemRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.services.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<ItemResource> getAllItems() {
        return menuItemRepository.findAll().stream()
                .map(ItemEntity::toItemResource)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResource createItem(ItemEntity body) throws Exception {
        try{
            return menuItemRepository.save(body).toItemResource();
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
