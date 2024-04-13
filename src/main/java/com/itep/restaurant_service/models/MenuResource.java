package com.itep.restaurant_service.models;

import com.itep.restaurant_service.repositories.entities.CategoryEntity;
import com.itep.restaurant_service.repositories.entities.ItemEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@Getter
@Builder
public class MenuResource {
    private long id;
    private String name;
    private long category;




}
