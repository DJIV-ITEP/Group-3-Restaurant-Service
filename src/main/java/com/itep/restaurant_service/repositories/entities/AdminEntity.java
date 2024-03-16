package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "admins")
public class AdminEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "username")
    private final String username;
    @Column(name = "password")
    private String password;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "admin_id")
    Collection<RestaurantEntity> restaurants;

    AdminEntity() {
        this( "", "");
    }

    public AdminEntity(String username,
                       String password) {
        this.username = username;
        this.password = password;
    }

}
