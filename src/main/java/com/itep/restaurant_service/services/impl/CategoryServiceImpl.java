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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl implements CategoryService {
//    private final CategoryRepository categoryRepository;
//    public CategoryServiceImpl(CategoryRepository categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Override
    public List<CategoryResource> getCategory(Long rest_id) {
        return categoryRepository.findByRestaurantId(rest_id).stream()
                .map(CategoryEntity::toCategoryResource)
                .collect(Collectors.toList());

    }

    @Override
    public CategoryResource createCategory(Long rest_id, CategoryEntity body) throws Exception {
        try{
            Optional<RestaurantEntity> yourEntityOptional = restaurantRepository.findById(rest_id);
            if (yourEntityOptional.isPresent()) {
                RestaurantEntity yourEntity = yourEntityOptional.get();
                body.setRestaurant(yourEntity);

                return categoryRepository.save(body).toCategoryResource();
            }
            else{
                throw new EntityNotFoundException("Restaurant with id " + rest_id + " not found");
            }

        }catch (Exception e){
            if(e.getMessage().contains("duplicate key value violates unique constraint")){
                throw new Exception("category with this name already exists");
            }
            else if (e.getMessage().contains("not-null property references a null")) {
                throw new Exception("You must provide all the category fields");
            }
            throw new Exception("unknown error");
        }
    }

    @Override
    public CategoryResource updateCategory(Long id, CategoryEntity body) throws Exception {
        CategoryEntity existCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("category not found"));
        existCategory.setName(body.getName());
        try {
            return categoryRepository.save(existCategory).toCategoryResource();
        } catch (Exception e) {
            throw new Exception("Failed to update menu");
        }
    }

    @Override
    public void deleteCategory(Long id) throws Exception {
        try{
            categoryRepository.deleteById(id);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());

        }
    }

}
