package com.itep.restaurant_service.models;

import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoryResource {
    private long id;
    private String name;
    private long restaurant;



}
