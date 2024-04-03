package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.MenuService;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;





    @Override
    public List<MenuResource> getMenues(long rest_id, long cat_id) throws Exception{
        Optional<CategoryEntity> yourEntityOptional = categoryRepository.findById(cat_id);
        if (yourEntityOptional .isPresent()) {
            CategoryEntity yourEntity = yourEntityOptional.get();
            if(yourEntity.getRestaurant().getId() == rest_id){
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
                throw new EntityNotFoundException("Category with id " + cat_id + " not belong to Restaurant");
            }
        }
        else{
            throw new EntityNotFoundException("Category with id " + cat_id + " not found");
        }


    }

    @Override
    public MenuResource createMenu(long rest_id, long cat_id, MenuEntity body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the current user
        String username = authentication.getName();
        Optional<CategoryEntity> yourEntityOptional = categoryRepository.findById(cat_id);
        if (yourEntityOptional .isPresent()) {
            CategoryEntity yourEntity = yourEntityOptional.get();
            if(yourEntity.getRestaurant().getId() == rest_id){
                if(Objects.equals(yourEntity.getRestaurant().getOwner().getUsername(), username)){
                    try{
                        body.setCategory(yourEntity);
                        return menuRepository.save(body).toMenuResource();
                    }
                    catch (Exception e){
                        if(e.getMessage().contains("duplicate key value violates unique constraint")){
                            throw new Exception("menu with this name already exists in category");
                        }
                        else if (e.getMessage().contains("not-null property references a null")) {
                            throw new Exception("You must provide all the category fields");
                        }
                        System.out.println(e.getMessage());
                        throw new Exception(e.getMessage());
                    }
                }
                else {
                    throw new EntityNotFoundException("you are not the owner of Restaurant");
                }

            }
            else{
                throw new EntityNotFoundException("Category with id " + cat_id + " not belong to Restaurant");
            }

        }
        else{
            throw new EntityNotFoundException("Category with id " + cat_id + " not found");
        }

    }

    @Override
    public MenuResource updateMenu(long rest_id, long cat_id,long id, MenuEntity body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the current user
        String username = authentication.getName();
        Optional<CategoryEntity> yourEntityOptional = categoryRepository.findById(cat_id);
        if (yourEntityOptional .isPresent()) {
            CategoryEntity yourEntity = yourEntityOptional.get();
            if(yourEntity.getRestaurant().getId() == rest_id){
                MenuEntity existMenu = menuRepository.findById(id)
                        .orElseThrow(() -> new Exception("Menu not found"));
                if(existMenu.getCategory().getId() == cat_id){
                    if(Objects.equals(yourEntity.getRestaurant().getOwner().getUsername(), username)){
                        try {
                            existMenu.setName(body.getName());
                            return menuRepository.save(existMenu).toMenuResource();
                        } catch (Exception e) {
                            throw new Exception("Failed to update menu");
                        }
                    }
                    else {
                        throw new EntityNotFoundException("you are not the owner of Restaurant");
                    }

                }
                else{
                    throw new EntityNotFoundException("Menu with id " + id + " not belong to Category");
                }
            }
            else{
                throw new EntityNotFoundException("Category with id " + cat_id + " not belong to Restaurant");
            }
        }
        else{
            throw new EntityNotFoundException("Category with id " + cat_id + " not found");
        }



    }

    @Override
    public void deleteMenu(long rest_id, long cat_id,long id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the current user
        String username = authentication.getName();
        Optional<CategoryEntity> yourEntityOptional = categoryRepository.findById(cat_id);
        if (yourEntityOptional .isPresent()) {
            CategoryEntity yourEntity = yourEntityOptional.get();
            if(yourEntity.getRestaurant().getId() == rest_id){
                MenuEntity existMenu = menuRepository.findById(id)
                        .orElseThrow(() -> new Exception("Menu not found"));
                if(existMenu.getCategory().getId() == cat_id){
                    if(Objects.equals(yourEntity.getRestaurant().getOwner().getUsername(), username)){
                        try {
                            menuRepository.deleteById(id);
                        } catch (Exception e) {
                            throw new Exception("Failed to update menu");
                        }
                    }
                    else {
                        throw new EntityNotFoundException("you are not the owner of Restaurant");
                    }

                }
                else{
                    throw new EntityNotFoundException("Menu with id " + id + " not belong to Category");
                }
            }
            else{
                throw new EntityNotFoundException("Category with id " + cat_id + " not belong to Restaurant");
            }
        }
        else{
            throw new EntityNotFoundException("Category with id " + cat_id + " not found");
        }


    }

}
