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
    public List<CategoryResource> getCategory(long restaurantId) throws Exception {
        Optional<RestaurantEntity> yourEntityOptional = restaurantRepository.findById(restaurantId);
        if (yourEntityOptional.isPresent()) {
            return categoryRepository.findByRestaurantId(restaurantId).stream()
                    .map(CategoryEntity::toCategoryResource)
                    .collect(Collectors.toList());
        }
        else{
            throw new Exception("Restaurant with id " + restaurantId + " not found");
        }

    }

    @Override
    public Optional<CategoryResource> getCategoryDetails(long restaurantId, long categoryId) throws Exception {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        if(categoryEntity.isPresent()){
            if(RestaurantUtils.isCategoryInRestaurant(categoryEntity.get(),restaurantId)){
                return categoryEntity.map(CategoryEntity::toCategoryResource);
            }
            else{
                throw new Exception("Category with id " + categoryId + " not in Restaurant");
            }
        }
        else{
            throw new Exception("Category with id " + categoryId + " not found");
        }



    }

    @Override
    public CategoryResource createCategory(long restaurantId, CategoryEntity body) throws Exception {

        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new Exception("Restaurant not found"));

        if(RestaurantUtils.isRestaurantOwner(restaurantEntity,SecurityContextHolder.getContext().getAuthentication().getName())){
            try{
                body.setRestaurant(restaurantEntity);

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
        else{
            throw new Exception("you are not the owner of Restaurant "+restaurantEntity.getName());
        }


    }

    @Override
    public CategoryResource updateCategory(long restaurantId,long id, CategoryEntity body) throws Exception {


        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("category not found"));

        if(RestaurantUtils.isCategoryInRestaurant(categoryEntity,restaurantId)){
            if(RestaurantUtils.isRestaurantOwner(categoryEntity.getRestaurant(),SecurityContextHolder.getContext().getAuthentication().getName())){
                if(!body.getName().isEmpty()){
                    categoryEntity.setName(body.getName());
                    try {
                        return categoryRepository.save(categoryEntity).toCategoryResource();
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }
                else{
                    throw new Exception("name is required");
                }
            }
            else{
                throw new Exception("you are not the owner of Restaurant");
            }
        }
        else{
            throw new Exception("Category not belong to Restaurant");
        }




    }

    @Override
    public void deleteCategory(long restaurantId,long id) throws Exception {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("category not found"));

        if(RestaurantUtils.isCategoryInRestaurant(categoryEntity,restaurantId)){
            if(RestaurantUtils.isRestaurantOwner(categoryEntity.getRestaurant(),SecurityContextHolder.getContext().getAuthentication().getName())){

                try {
                    categoryRepository.deleteById(id);
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }


            }
            else{
                throw new Exception("you are not the owner of Restaurant");
            }
        }
        else{
            throw new Exception("Category not belong to Restaurant");
        }




    }

}
