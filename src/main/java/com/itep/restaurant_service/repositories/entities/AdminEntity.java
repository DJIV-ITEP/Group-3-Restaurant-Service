package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collection;

@Entity
@Table(name = "admins")
public class AdminEntity {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    Collection<RestaurantEntity> restaurants;

    AdminEntity() {
    }

    public AdminEntity(String username,
                       String password) {
        this.username = username;
        this.password = password;
    }

}
