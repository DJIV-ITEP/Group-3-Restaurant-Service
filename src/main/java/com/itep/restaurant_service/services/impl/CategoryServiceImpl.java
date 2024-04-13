package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.CategoryService;
import com.itep.restaurant_service.services.impl.errorsHandels.CategoryNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.CategoryNotInRestaurantException;
import com.itep.restaurant_service.services.impl.errorsHandels.RestaurantNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.UserNotOwnerOfRestaurantException;
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



    final private CategoryRepository categoryRepository;


    final private RestaurantRepository restaurantRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, RestaurantRepository restaurantRepository){
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
    }
    @Override
    public List<CategoryResource> getCategory(long restaurantId) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
        return categoryRepository.findByRestaurantId(restaurantId).stream()
                .map(CategoryEntity::toCategoryResource)
                .collect(Collectors.toList());


    }

    @Override
    public CategoryResource getCategoryDetails(long restaurantId, long categoryId) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotInRestaurantException(categoryId));

        if(RestaurantUtils.isCategoryInRestaurant(categoryEntity,restaurantId)){
            return categoryEntity.toCategoryResource();
        }
        else{
            throw new CategoryNotInRestaurantException(categoryId );
        }





    }

    @Override
    public CategoryResource createCategory(long restaurantId, CategoryEntity body) throws Exception {

        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        if(RestaurantUtils.isRestaurantOwner(restaurantEntity,SecurityContextHolder.getContext().getAuthentication().getName())){
            try{
                body.setRestaurant(restaurantEntity);
                CategoryEntity categoryEntity = categoryRepository.save(body);
                List <CategoryEntity> cat = restaurantEntity.getCategories();
                cat.add(categoryEntity);
                restaurantEntity.setCategories(cat);

                return categoryEntity.toCategoryResource();
            }
            catch (Exception e){
                if(e.getMessage().contains("duplicate key value violates unique constraint")){
                    throw new Exception("category with this name already exists");
                }
                else if (e.getMessage().contains("not-null property references a null")) {
                    throw new Exception("You must provide all the category fields");
                }
                throw new Exception(e.getMessage());
            }
        }
        else{
            throw new UserNotOwnerOfRestaurantException();
        }


    }

    @Override
    public CategoryResource updateCategory(long restaurantId,long id, CategoryEntity body) throws Exception {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(id));



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
                throw new UserNotOwnerOfRestaurantException();
            }
        }
        else{
            throw new CategoryNotInRestaurantException(id);
        }




    }

    @Override
    public void deleteCategory(long restaurantId,long id) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotInRestaurantException(id));



        if(RestaurantUtils.isCategoryInRestaurant(categoryEntity,restaurantId)){
            if(RestaurantUtils.isRestaurantOwner(categoryEntity.getRestaurant(),SecurityContextHolder.getContext().getAuthentication().getName())){

                try {
                    categoryRepository.deleteById(id);
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }


            }
            else{
                throw new UserNotOwnerOfRestaurantException();
            }
        }
        else{
            throw new CategoryNotInRestaurantException(id);
        }




    }

}
