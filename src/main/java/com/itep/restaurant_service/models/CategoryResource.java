package com.itep.restaurant_service.models;

import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import lombok.*;


@AllArgsConstructor
@Getter
@Builder
public class CategoryResource {
    private long id;
    private String name;
    private long restaurant;



}
