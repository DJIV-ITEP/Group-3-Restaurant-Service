package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class ItemEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private double price;

    ItemEntity() {
    }

    public ItemEntity(String name,
                      double price) {
        this.name = name;
        this.price = price;
    }

}
