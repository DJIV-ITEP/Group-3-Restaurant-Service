package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Collection<MenuEntity> menus;

    CategoryEntity() {
    }

    public CategoryEntity(String name) {
        this.name = name;
    }

}
