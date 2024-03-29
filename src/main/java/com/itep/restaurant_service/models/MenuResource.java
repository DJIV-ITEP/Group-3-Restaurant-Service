package com.itep.restaurant_service.models;

import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MenuResource {
    private long id;
    private String name;
    private long category;
    private List<ItemEntity> items;
    public MenuResource(Long id, String name){}
    public MenuResource(Long id, String name, long category){}

    public MenuResource(Long id, String name, long category, List<ItemEntity> items){
        id = id;
        name = name;
        category = category;
        items = items;
    }


}
