package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "menus")
public class MenuEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id")
    private Collection<ItemEntity> items;

    MenuEntity() {
    }

    public MenuEntity(String name) {
        this.name = name;
    }

}
