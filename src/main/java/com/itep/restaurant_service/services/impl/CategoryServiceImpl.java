package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Override
    public List<CategoryResource> getCategory(long restaurantId) {
        Optional<RestaurantEntity> yourEntityOptional = restaurantRepository.findById(restaurantId);
        if (yourEntityOptional.isPresent()) {
            return categoryRepository.findByRestaurantId(restaurantId).stream()
                    .map(CategoryEntity::toCategoryResource)
                    .collect(Collectors.toList());
        }
        else{
            throw new EntityNotFoundException("Restaurant with id " + restaurantId + " not found");
        }

    }

    @Override
    public Optional<CategoryResource> getCategoryDetails(long restaurantId, long categoryId) {
        Optional<RestaurantEntity> yourEntityOptional = restaurantRepository.findById(restaurantId);
        if (yourEntityOptional.isPresent()) {
            Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
            if(categoryEntity.isPresent()){
                return categoryEntity.map(CategoryEntity::toCategoryResource);
            }
            else {
                throw new EntityNotFoundException("Category with id " + categoryId + " not found");
            }

        }
        else{
            throw new EntityNotFoundException("Restaurant with id " + restaurantId + " not found");
        }

    }

    @Override
    public CategoryResource createCategory(long restaurantId, CategoryEntity body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the current user
        String username = authentication.getName();
        Optional<RestaurantEntity> yourEntityOptional = restaurantRepository.findById(restaurantId);
        if (yourEntityOptional.isPresent()) {
            RestaurantEntity yourEntity = yourEntityOptional.get();
            if(Objects.equals(yourEntity.getOwner().getUsername(), username)){
                try{
                    body.setRestaurant(yourEntity);

                    return categoryRepository.save(body).toCategoryResource();
                }
                catch (Exception e){
                    if(e.getMessage().contains("duplicate key value violates unique constraint")){
                        throw new Exception("category with this name already exists");
                    }
                    else if (e.getMessage().contains("not-null property references a null")) {
                        throw new Exception("You must provide all the category fields");
                    }
                    throw new Exception("unknown error");
                }

            }
            else {
                throw new EntityNotFoundException("you are not the owner of Restaurant "+yourEntity.getName());
            }

        }
        else{
            throw new EntityNotFoundException("Restaurant with id " + restaurantId + " not found");
        }

    }

    @Override
    public CategoryResource updateCategory(long restaurantId,long id, CategoryEntity body) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the current user
        String username = authentication.getName();


        CategoryEntity existCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("category not found"));
        if(existCategory.getRestaurant().getId() == restaurantId){
            if(Objects.equals(existCategory.getRestaurant().getOwner().getUsername(), username)){
                existCategory.setName(body.getName());
                try {
                    return categoryRepository.save(existCategory).toCategoryResource();
                } catch (Exception e) {
                    throw new Exception("Failed to update menu");
                }
            }
            else{
                throw new EntityNotFoundException("you are not the owner of Restaurant");
            }
        }
        else{
            throw new EntityNotFoundException("Category not belong to Restaurant");
        }


    }

    @Override
    public void deleteCategory(long restaurantId,long id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the current user
        String username = authentication.getName();


        CategoryEntity existCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("category not found"));

        if(existCategory.getRestaurant().getId() == restaurantId){
            if(Objects.equals(existCategory.getRestaurant().getOwner().getUsername(), username)){
                try{
                    categoryRepository.deleteById(id);
                }
                catch (Exception e){
                    throw new Exception(e.getMessage());

                }
            }
            else{
                throw new EntityNotFoundException("you are not the owner of Restaurant");
            }
        }
        else{
            throw new EntityNotFoundException("Category not belong to Restaurant");
        }


    }

}
