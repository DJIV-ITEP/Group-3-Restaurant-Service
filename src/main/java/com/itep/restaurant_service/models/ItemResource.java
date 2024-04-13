package com.itep.restaurant_service.models;

import com.itep.restaurant_service.repositories.entities.MenuEntity;
import lombok.*;


@AllArgsConstructor
@Getter
@Builder
public class ItemResource {

    private long id;
    private String name;
    private double price;
    private double quantity;
    private String description;
    private long menu;


}
