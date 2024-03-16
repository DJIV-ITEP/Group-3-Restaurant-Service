package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collection;

@Entity
@Table(name = "menus")
public class MenuEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<ItemEntity> items;

    MenuEntity() {
    }

    public MenuEntity(String name) {
        this.name = name;
    }

}
