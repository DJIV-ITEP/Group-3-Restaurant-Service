package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.MenuService;
import com.itep.restaurant_service.services.impl.errorsHandels.*;
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
public class MenuServiceImpl implements MenuService {

    final private CategoryRepository categoryRepository;

    final private MenuRepository menuRepository;

    final private RestaurantRepository restaurantRepository;

    public MenuServiceImpl(RestaurantRepository restaurantRepository,CategoryRepository categoryRepository, MenuRepository menuRepository){
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.menuRepository = menuRepository;
    }



    @Override
    public List<MenuResource> getMenues(long rest_id, long cat_id) throws Exception{
        RestaurantEntity restaurant = restaurantRepository.findById(rest_id)
                .orElseThrow(() -> new RestaurantNotFoundException(rest_id));

        CategoryEntity categoryEntity = categoryRepository.findById(cat_id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(cat_id));

        if(RestaurantUtils.isCategoryInRestaurant(categoryEntity,rest_id)){
            try{
                return menuRepository.findByCategoryId(cat_id).stream()
                        .map(MenuEntity::toMenuResource)
                        .collect(Collectors.toList());
            }
            catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }
        else{
            throw new CategoryNotInRestaurantException(cat_id);
        }


    }
    @Override
    public MenuResource getMenueDetails(long rest_id, long cat_id, long menu_id) throws Exception{
        RestaurantEntity restaurant = restaurantRepository.findById(rest_id)
                .orElseThrow(() -> new RestaurantNotFoundException(rest_id));

        CategoryEntity categoryEntity = categoryRepository.findById(cat_id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(cat_id));
        MenuEntity menuEntity = menuRepository.findById(menu_id)
                .orElseThrow(() -> new MenuNotFoundException(menu_id));

        if(RestaurantUtils.isMenuInCategory(menuEntity,cat_id)){
            if(RestaurantUtils.isCategoryInRestaurant(menuEntity.getCategory(), rest_id)){
                try{

                    return menuEntity.toMenuResource();
                }
                catch (Exception e){
                    throw new Exception(e.getMessage());
                }
            }
            else{
                throw new CategoryNotInRestaurantException(cat_id);
            }
        }
        else{
            throw new MenuNotInCategoryException(menu_id);
        }


    }

    @Override
    public MenuResource createMenu(long rest_id, long cat_id, MenuEntity body) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(rest_id)
                .orElseThrow(() -> new RestaurantNotFoundException(rest_id));

        CategoryEntity categoryEntity = categoryRepository.findById(cat_id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(cat_id));
        if(RestaurantUtils.isCategoryInRestaurant(categoryEntity,rest_id)){
            if(RestaurantUtils.isRestaurantOwner(categoryEntity.getRestaurant(),SecurityContextHolder.getContext().getAuthentication().getName())){
                try{
                    body.setCategory(categoryEntity);
                    MenuEntity menuEntity= menuRepository.save(body);
                    List<MenuEntity> men = categoryEntity.getMenus();
                    men.add(menuEntity);
                    categoryEntity.setMenus(men);
                    return menuEntity.toMenuResource();
                }
                catch (Exception e){
                    if(e.getMessage().contains("duplicate key value violates unique constraint")){
                        throw new Exception("menu with this name already exists in category");
                    }
                    else if (e.getMessage().contains("not-null property references a null")) {
                        throw new Exception("You must provide all the menu fields");
                    }
                    System.out.println(e.getMessage());
                    throw new Exception(e.getMessage());
                }
            }
            else{
                throw new UserNotOwnerOfRestaurantException();
            }
        }
        else{
            throw new CategoryNotInRestaurantException(cat_id);
        }


    }

    @Override
    public MenuResource updateMenu(long rest_id, long cat_id,long id, MenuEntity body) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(rest_id)
                .orElseThrow(() -> new RestaurantNotFoundException(rest_id));

        CategoryEntity categoryEntity = categoryRepository.findById(cat_id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(cat_id));
        MenuEntity menuEntity = menuRepository.findById(id)
                .orElseThrow(() -> new MenuNotFoundException(id));
        if(RestaurantUtils.isMenuInCategory(menuEntity,cat_id)){
            if(RestaurantUtils.isCategoryInRestaurant(menuEntity.getCategory(), rest_id)){
                if(RestaurantUtils.isRestaurantOwner(menuEntity.getCategory().getRestaurant(),SecurityContextHolder.getContext().getAuthentication().getName() )){
                    if(!body.getName().isEmpty()){
                        try {
                            menuEntity.setName(body.getName());
                            return menuRepository.save(menuEntity).toMenuResource();
                        } catch (Exception e) {
                            throw new Exception(e.getMessage());
                        }
                    }
                    else{
                        throw new Exception("name is required");
                    }

                }
                else {
                    throw new UserNotOwnerOfRestaurantException();
                }
            }
            else{
                throw new CategoryNotInRestaurantException( cat_id );
            }
        }
        else{
            throw new MenuNotInCategoryException( id );
        }





    }

    @Override
    public void deleteMenu(long rest_id, long cat_id,long id) throws Exception {

        RestaurantEntity restaurant = restaurantRepository.findById(rest_id)
                .orElseThrow(() -> new RestaurantNotFoundException(rest_id));

        CategoryEntity categoryEntity = categoryRepository.findById(cat_id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(cat_id));
        MenuEntity menuEntity = menuRepository.findById(id)
                .orElseThrow(() -> new MenuNotFoundException(id));
        if(RestaurantUtils.isMenuInCategory(menuEntity,cat_id)){
            if(RestaurantUtils.isCategoryInRestaurant(menuEntity.getCategory(), rest_id)){
                if(RestaurantUtils.isRestaurantOwner(menuEntity.getCategory().getRestaurant(),SecurityContextHolder.getContext().getAuthentication().getName() )){
                    try {
                        menuRepository.deleteById(id);
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }

                }
                else {
                    throw new UserNotOwnerOfRestaurantException();
                }
            }
            else{
                throw new CategoryNotInRestaurantException(cat_id );
            }
        }
        else{
            throw new MenuNotInCategoryException( id );
        }




    }

}
