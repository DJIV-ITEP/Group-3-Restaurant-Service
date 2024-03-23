package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.models.MenuResource;
import com.itep.restaurant_service.repositories.MenuRepository;
import com.itep.restaurant_service.repositories.entities.MenuEntity;
import com.itep.restaurant_service.services.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }






    @Override
    public List<MenuResource> getAllMenues() {
        return menuRepository.findAll().stream()
                .map(MenuEntity::toMenuResource)
                .collect(Collectors.toList());
    }

    @Override
    public MenuResource createMenu(MenuEntity body) throws Exception {
        try{
            return menuRepository.save(body).toMenuResource();
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
