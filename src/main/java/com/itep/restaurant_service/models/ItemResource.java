package com.itep.restaurant_service.models;

import com.itep.restaurant_service.repositories.entities.MenuEntity;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@Builder
public class ItemResource {

    private long id;
    private String name;
    private double price;
    private double quantity;
    private String description;
    private long menu;
    public ItemResource(){}
    public ItemResource(long id, String name, String description, double price, double quantity){}
    public ItemResource(long id, String name, String description, double price, double quantity, long menu){}

}
