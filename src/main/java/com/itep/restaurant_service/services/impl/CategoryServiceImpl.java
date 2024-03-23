package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.CategoryResource;
import com.itep.restaurant_service.repositories.CategoryRepository;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResource> getCategory(Long rest_id) {
//        return categoryRepository.findBy()

        return categoryRepository.findAll().stream()
                .map(CategoryEntity::toCategoryResource)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResource createCategory(CategoryEntity body) throws Exception {
        try{
            return categoryRepository.save(body).toCategoryResource();
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
