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

import java.util.ArrayList;
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
        MenuEntity menuEntity = menuRepository.findById(menu_id)
                .orElseThrow(() -> new Exception("menu not found"));

        if(RestaurantUtils.isMenuInCategory(menuEntity, cat_id)){
            if(RestaurantUtils.isCategoryInRestaurant(menuEntity.getCategory(), rest_id)){
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
                throw new Exception("Category with id " + cat_id + " not belong to Restaurant");
            }
        }
        else{
            throw new Exception("Menu with id " + menu_id + " not belong to Category");
        }

    }

    @Override
    public List<ItemResource> getItemsbyIds(Integer[] itemsIds) throws Exception {
        List<ItemResource> result = new ArrayList<>();;
        for (Integer itemsId : itemsIds) {
            try {
                Optional<ItemEntity> itemEntityOptional = menuItemRepository.findById(Long.valueOf(itemsId));
                if (itemEntityOptional.isPresent()) {
                    ItemEntity itemEntity = itemEntityOptional.get();
                    result.add(itemEntity.toItemResource());
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return result;

    }

    @Override
    public Optional<ItemResource> getItemsDetails(long rest_id, long cat_id, long menu_id, long item_id) throws Exception {
        ItemEntity itemEntity = menuItemRepository.findById(item_id)
                .orElseThrow(() -> new Exception("item not found"));
        if(RestaurantUtils.isItemInMenu(itemEntity,menu_id)){
            if(RestaurantUtils.isMenuInCategory(itemEntity.getMenu(),cat_id)){
                if(RestaurantUtils.isCategoryInRestaurant(itemEntity.getMenu().getCategory(), rest_id)){
                    try{
                        Optional<ItemEntity> yourEntityOptional = menuItemRepository.findById(item_id);
                        return yourEntityOptional.map(ItemEntity::toItemResource);
                    }
                    catch (Exception e){
                        throw new Exception(e.getMessage());
                    }
                }
                else{
                    throw new Exception("Category with id " + cat_id + " not belong to Restaurant");
                }
            }
            else{
                throw new Exception("Menu with id " + menu_id + " not belong to Category");
            }
        }
        else{
            throw new Exception("item with id " + item_id + " not belong to menu");
        }





    }

    @Override
    public ItemResource createItem(long rest_id,long cat_id,long menu_id, ItemEntity body) throws Exception {
        MenuEntity menuEntity = menuRepository.findById(menu_id)
                .orElseThrow(() -> new Exception("menu not found"));

        if(RestaurantUtils.isMenuInCategory(menuEntity, cat_id)){
            if(RestaurantUtils.isCategoryInRestaurant(menuEntity.getCategory(), rest_id)){
                if(RestaurantUtils.isRestaurantOwner(menuEntity.getCategory().getRestaurant(),SecurityContextHolder.getContext().getAuthentication().getName())){
                    try{
                        body.setMenu(menuEntity);
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
                    throw new Exception("you are not the owner of Restaurant");
                }
            }
            else{
                throw new Exception("Category with id " + cat_id + " not belong to Restaurant");
            }
        }
        else{
            throw new Exception("Menu with id " + menu_id + " not belong to Category");
        }




    }

    @Override
    public ItemResource updateItem(long rest_id,long cat_id,long menu_id,long id, ItemEntity body) throws Exception {
        ItemEntity itemEntity = menuItemRepository.findById(id)
                .orElseThrow(() -> new Exception("item not found"));

        if(RestaurantUtils.isItemInMenu(itemEntity, menu_id)){
            if(RestaurantUtils.isMenuInCategory(itemEntity.getMenu(),cat_id)){
                if(RestaurantUtils.isCategoryInRestaurant(itemEntity.getMenu().getCategory(), rest_id)){
                    if(RestaurantUtils.isRestaurantOwner(itemEntity.getMenu().getCategory().getRestaurant(), SecurityContextHolder.getContext().getAuthentication().getName())){
                        try{
                            if(body.getName().isEmpty()){
                                throw new Exception("name is required");
                            }
                            else if(body.getDescription().isEmpty()){
                                throw new Exception("description is required");
                            }
                            else if(body.getPrice() < 1){
                                throw new Exception("price must be more than 0");
                            }
                            itemEntity.setName(body.getName());
                            itemEntity.setDescription(body.getDescription());
                            itemEntity.setPrice(body.getPrice());
                            itemEntity.setQuantity(body.getQuantity());
                            return menuItemRepository.save(itemEntity).toItemResource();
                        }
                        catch (Exception e) {
                            throw new Exception("Failed to update item");
                        }
                    }
                    else{
                        throw new Exception("you are not the owner of Restaurant");
                    }
                }
                else{
                    throw new Exception("Category with id " + cat_id + " not belong to Restaurant");
                }
            }
            else{
                throw new Exception("Menu with id " + menu_id + " not belong to Category");
            }
        }
        else{
            throw new Exception("item with id " + id + " not belong to Menu");
        }






    }

    @Override
    public void deleteItem(long rest_id,long cat_id,long menu_id,long id) throws Exception {

        ItemEntity itemEntity = menuItemRepository.findById(id)
                .orElseThrow(() -> new Exception("item not found"));

        if(RestaurantUtils.isItemInMenu(itemEntity, menu_id)){
            if(RestaurantUtils.isMenuInCategory(itemEntity.getMenu(),cat_id)){
                if(RestaurantUtils.isCategoryInRestaurant(itemEntity.getMenu().getCategory(), rest_id)){
                    if(RestaurantUtils.isRestaurantOwner(itemEntity.getMenu().getCategory().getRestaurant(), SecurityContextHolder.getContext().getAuthentication().getName())){
                        try{
                            menuItemRepository.deleteById(id);
                        }
                        catch (Exception e) {
                            throw new Exception(e.getMessage());
                        }
                    }
                    else{
                        throw new Exception("you are not the owner of Restaurant");
                    }
                }
                else{
                    throw new Exception("Category with id " + cat_id + " not belong to Restaurant");
                }
            }
            else{
                throw new Exception("Menu with id " + menu_id + " not belong to Category");
            }
        }
        else{
            throw new Exception("item with id " + id + " not belong to Menu");
        }



    }
}
