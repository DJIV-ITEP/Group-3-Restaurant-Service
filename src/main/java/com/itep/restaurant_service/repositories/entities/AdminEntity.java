package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collection;

@Entity
@Table(name = "admins")
public class AdminEntity {
    @Id
    @Column(name = "username")
    public final String username;
    @Column(name = "password")
    public String password;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
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
