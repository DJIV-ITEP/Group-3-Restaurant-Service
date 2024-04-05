package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.CategoryResource;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<ItemResource> getItems(long rest_id, long cat_id,long menu_id) throws Exception {
        Optional<MenuEntity> yourEntityOptional = menuRepository.findById(menu_id);
        if (yourEntityOptional.isPresent()) {
            MenuEntity yourEntity = yourEntityOptional.get();
            if(yourEntity.getCategory().getId() == cat_id){
                if(yourEntity.getCategory().getRestaurant().getId() == rest_id){
                    try{
                        return menuItemRepository.findByMenuId(menu_id).stream()
                                .map(ItemEntity::toItemResource)
                                .collect(Collectors.toList());
                    }
                    catch (Exception e){
                        throw new Exception(e.getMessage());
                    }

                }
                else{
                    throw new EntityNotFoundException("Category with id " + cat_id + " not belong to Restaurant");
                }
            }
            else{
                throw new EntityNotFoundException("Menu with id " + menu_id + " not belong to Category");
            }
        }
        else{
            throw new EntityNotFoundException("Menu with id " + menu_id + " not found");
        }

    }

    @Override
    public Optional<ItemResource> getItemsDetails(long rest_id, long cat_id, long menu_id, long item_id) throws Exception {
        Optional<ItemEntity> yourEntityOptional = menuItemRepository.findById(item_id);
        if (yourEntityOptional.isPresent()) {
            ItemEntity yourEntity = yourEntityOptional.get();
            if(yourEntity.getMenu().getId() == menu_id){
                if(yourEntity.getMenu().getCategory().getId() == cat_id){
                    if(yourEntity.getMenu().getCategory().getRestaurant().getId() == rest_id){
                        try{
                            return yourEntityOptional.map(ItemEntity::toItemResource);
                        }
                        catch (Exception e){
                            throw new Exception(e.getMessage());
                        }

                    }
                    else{
                        throw new EntityNotFoundException("Category with id " + cat_id + " not belong to Restaurant");
                    }
                }
                else{
                    throw new EntityNotFoundException("Menu with id " + menu_id + " not belong to Category");
                }
            }
            else{
                throw new EntityNotFoundException("item with id " + item_id + " not belong to menu");
            }

        }
        else{
            throw new EntityNotFoundException("item with id " + item_id + " not found");
        }

    }

    @Override
    public ItemResource createItem(long rest_id,long cat_id,long menu_id, ItemEntity body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the current user
        String username = authentication.getName();
        Optional<MenuEntity> yourEntityOptional = menuRepository.findById(menu_id);
        if (yourEntityOptional.isPresent()) {
            MenuEntity yourEntity = yourEntityOptional.get();
            if(yourEntity.getCategory().getId() == cat_id){
                if(yourEntity.getCategory().getRestaurant().getId() == rest_id){
                    if(Objects.equals(yourEntity.getCategory().getRestaurant().getOwner().getUsername(), username)){
                        try{
                            body.setMenu(yourEntity);
                            return menuItemRepository.save(body).toItemResource();

                        }
                        catch (Exception e){
                            if(e.getMessage().contains("duplicate key value violates unique constraint")){
                                throw new Exception("item already exists");
                            }
                            else if (e.getMessage().contains("not-null property references a null")) {
                                throw new Exception("You must provide all the item fields");
                            }
                            throw new Exception("unknown error");
                        }
                    }
                    else{
                        throw new EntityNotFoundException("you are not the owner of Restaurant");
                    }

                }
                else{
                    throw new EntityNotFoundException("Category with id " + cat_id + " not belong to Restaurant");
                }
            }
            else{
                throw new EntityNotFoundException("Menu with id " + menu_id + " not belong to Category");
            }

        }
        else{
            throw new EntityNotFoundException("Menu with id " + menu_id + " not found");
        }





    }

    @Override
    public ItemResource updateItem(long rest_id,long cat_id,long menu_id,long id, ItemEntity body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the current user
        String username = authentication.getName();
        ItemEntity existItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new Exception("item not found"));
        if(existItem.getMenu().getId() == menu_id){
            if(existItem.getMenu().getCategory().getId() == cat_id){
                if(existItem.getMenu().getCategory().getRestaurant().getId() == rest_id){
                    if(Objects.equals(existItem.getMenu().getCategory().getRestaurant().getOwner().getUsername(), username)){
                        try{
                            existItem.setName(body.getName());
                            existItem.setDescription(body.getDescription());
                            existItem.setPrice(body.getPrice());
                            return menuItemRepository.save(existItem).toItemResource();
                        }
                        catch (Exception e) {
                            throw new Exception("Failed to update item");
                        }
                    }
                    else{
                        throw new EntityNotFoundException("you are not the owner of Restaurant");
                    }

                }
                else{
                    throw new EntityNotFoundException("Category with id " + cat_id + " not belong to Restaurant");
                }
            }
            else{
                throw new EntityNotFoundException("Menu with id " + menu_id + " not belong to Category");
            }
        }
        else{
            throw new EntityNotFoundException("item with id " + id + " not belong to Menu");
        }


    }

    @Override
    public void deleteItem(long rest_id,long cat_id,long menu_id,long id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the current user
        String username = authentication.getName();
        Optional<ItemEntity> yourEntityOptional = menuItemRepository.findById(id);
        if (yourEntityOptional.isPresent()) {
            ItemEntity yourEntity = yourEntityOptional.get();
            if(yourEntity.getMenu().getId() == menu_id){
                if(yourEntity.getMenu().getCategory().getId() == cat_id){
                    if(yourEntity.getMenu().getCategory().getRestaurant().getId() == rest_id){
                        if(Objects.equals(yourEntity.getMenu().getCategory().getRestaurant().getOwner().getUsername(), username)){
                            try{
                                menuItemRepository.deleteById(id);
                            }
                            catch (Exception e){
                                throw new Exception(e.getMessage());
                            }
                        }
                        else{
                            throw new EntityNotFoundException("you are not the owner of Restaurant");
                        }


                    }
                    else {
                        throw new EntityNotFoundException("Category with id " + cat_id + " not belong to Restaurant");
                    }

                }
                else {
                    throw new EntityNotFoundException("Menu with id " + menu_id + " not belong to Category");
                }

            }
            else {
                throw new EntityNotFoundException("item with id " + id + " not belong to Menu");
            }
        }
        else{
            throw new EntityNotFoundException("item with id " + id + " not found");
        }

    }
}
