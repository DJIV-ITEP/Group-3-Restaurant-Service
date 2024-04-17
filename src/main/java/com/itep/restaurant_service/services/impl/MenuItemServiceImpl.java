package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.models.ItemResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.MenuItemRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.MenuItemService;
import com.itep.restaurant_service.services.impl.errorsHandels.*;
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
    final private MenuItemRepository menuItemRepository;
    final private RestaurantRepository restaurantRepository;

    final private MenuRepository menuRepository;
    final private CategoryRepository categoryRepository;

    public MenuItemServiceImpl(RestaurantRepository restaurantRepository,CategoryRepository categoryRepository,
                               MenuRepository menuRepository, MenuItemRepository menuItemRepository){
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemRepository;
    }
    @Override
    public List<ItemResource> getItems(long rest_id, long cat_id,long menu_id) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(rest_id)
                .orElseThrow(() -> new RestaurantNotFoundException(rest_id));

        CategoryEntity categoryEntity = categoryRepository.findById(cat_id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(cat_id));

        MenuEntity menuEntity = menuRepository.findById(menu_id)
                .orElseThrow(() -> new MenuNotFoundException(menu_id));

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
                throw new CategoryNotInRestaurantException( cat_id );
            }
        }
        else{
            throw new MenuNotInCategoryException( menu_id );
        }

    }

    @Override
    public List<ItemResource> getItemsbyIds(Integer[] itemsIds) throws Exception {
        List<ItemResource> result = new ArrayList<>();;
        for (Integer itemsId : itemsIds) {
            try {
                Optional<ItemEntity> itemEntityOptional = menuItemRepository.findById(itemsId.longValue());
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
    public ItemResource getItemsDetails(long rest_id, long cat_id, long menu_id, long item_id) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(rest_id)
                .orElseThrow(() -> new RestaurantNotFoundException(rest_id));

        CategoryEntity categoryEntity = categoryRepository.findById(cat_id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(cat_id));

        MenuEntity menuEntity = menuRepository.findById(menu_id)
                .orElseThrow(() -> new MenuNotFoundException(menu_id));
        ItemEntity itemEntity = menuItemRepository.findById(item_id)
                .orElseThrow(() -> new ItemNotFoundException(item_id));

        if(RestaurantUtils.isItemInMenu(itemEntity,menu_id)){
            if(RestaurantUtils.isMenuInCategory(itemEntity.getMenu(),cat_id)){
                if(RestaurantUtils.isCategoryInRestaurant(itemEntity.getMenu().getCategory(), rest_id)){
                    try{

                        return itemEntity.toItemResource();
                    }
                    catch (Exception e){
                        throw new Exception(e.getMessage());
                    }
                }
                else{
                    throw new CategoryNotInRestaurantException(cat_id );
                }
            }
            else{
                throw new MenuNotInCategoryException(menu_id );
            }
        }
        else{
            throw new ItemNotInMenuException( item_id );
        }





    }

    @Override
    public ItemResource createItem(long rest_id,long cat_id,long menu_id, ItemEntity body) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(rest_id)
                .orElseThrow(() -> new RestaurantNotFoundException(rest_id));

        CategoryEntity categoryEntity = categoryRepository.findById(cat_id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(cat_id));

        MenuEntity menuEntity = menuRepository.findById(menu_id)
                .orElseThrow(() -> new MenuNotFoundException(menu_id));

        if(RestaurantUtils.isMenuInCategory(menuEntity, cat_id)){
            if(RestaurantUtils.isCategoryInRestaurant(menuEntity.getCategory(), rest_id)){
                if(RestaurantUtils.isRestaurantOwner(menuEntity.getCategory().getRestaurant(),SecurityContextHolder.getContext().getAuthentication().getName())){
                    try{
                        body.setMenu(menuEntity);
                        ItemEntity itemEntity = menuItemRepository.save(body);
                        List<ItemEntity> items = menuEntity.getItems();
                        items.add(itemEntity);
                        menuEntity.setItems(items);
                        return itemEntity.toItemResource();

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
                    throw new UserNotOwnerOfRestaurantException();
                }
            }
            else{
                throw new CategoryNotInRestaurantException( cat_id );
            }
        }
        else{
            throw new MenuNotInCategoryException( menu_id );
        }




    }

    @Override
    public ItemResource updateItem(long rest_id,long cat_id,long menu_id,long id, ItemEntity body) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(rest_id)
                .orElseThrow(() -> new RestaurantNotFoundException(rest_id));

        CategoryEntity categoryEntity = categoryRepository.findById(cat_id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(cat_id));

        MenuEntity menuEntity = menuRepository.findById(menu_id)
                .orElseThrow(() -> new MenuNotFoundException(menu_id));

        ItemEntity itemEntity = menuItemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

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
                        throw new UserNotOwnerOfRestaurantException();
                    }
                }
                else{
                    throw new CategoryNotInRestaurantException( cat_id );
                }
            }
            else{
                throw new MenuNotInCategoryException(menu_id);
            }
        }
        else{
            throw new ItemNotInMenuException( id );
        }






    }

    @Override
    public void deleteItem(long rest_id,long cat_id,long menu_id,long id) throws Exception {

        RestaurantEntity restaurant = restaurantRepository.findById(rest_id)
                .orElseThrow(() -> new RestaurantNotFoundException(rest_id));

        CategoryEntity categoryEntity = categoryRepository.findById(cat_id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(cat_id));

        MenuEntity menuEntity = menuRepository.findById(menu_id)
                .orElseThrow(() -> new MenuNotFoundException(menu_id));

        ItemEntity itemEntity = menuItemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

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
                        throw new UserNotOwnerOfRestaurantException();
                    }
                }
                else{
                    throw new CategoryNotInRestaurantException(cat_id );
                }
            }
            else{
                throw new MenuNotInCategoryException( menu_id );
            }
        }
        else{
            throw new ItemNotInMenuException( id );
        }



    }
}
