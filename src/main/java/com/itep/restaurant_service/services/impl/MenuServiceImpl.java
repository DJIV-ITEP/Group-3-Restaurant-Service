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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuRepository menuRepository;






    @Override
    public List<MenuResource> getAllMenues(Long cat_id) {
        return menuRepository.findByCategoryId(cat_id).stream()
                .map(MenuEntity::toMenuResource)
                .collect(Collectors.toList());
    }

    @Override
    public MenuResource createMenu(Long cat_id, MenuEntity body) throws Exception {
        try{
            Optional<CategoryEntity> yourEntityOptional = categoryRepository.findById(cat_id);
            if (yourEntityOptional.isPresent()) {
                CategoryEntity yourEntity = yourEntityOptional.get();
                body.setCategory(yourEntity);

                return menuRepository.save(body).toMenuResource();
            }
            else{
                throw new EntityNotFoundException("Category with id " + cat_id + " not found");
            }

        }catch (Exception e){
            if(e.getMessage().contains("duplicate key value violates unique constraint")){
                throw new Exception("restaurant with this username already exists");
            }
            else if (e.getMessage().contains("not-null property references a null")) {
                throw new Exception("You must provide all the restaurant fields");
            }
            throw new Exception("unknown error");
        }

    }

    @Override
    public MenuResource updateMenu(Long id, MenuEntity body) throws Exception {
        MenuEntity existMenu = menuRepository.findById(id)
                .orElseThrow(() -> new Exception("Menu not found"));
        existMenu.setName(body.getName());
        try {
            return menuRepository.save(existMenu).toMenuResource();
        } catch (Exception e) {
            throw new Exception("Failed to update menu");
        }

    }

    @Override
    public void deleteMenu(Long id) throws Exception {
        try{
            menuRepository.deleteById(id);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());

        }

    }

}
