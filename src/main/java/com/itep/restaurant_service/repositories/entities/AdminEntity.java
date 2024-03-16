package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;

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

    AdminEntity() {
        this( "", "");
    }

    public AdminEntity(String username,
                       String password) {
        this.username = username;
        this.password = password;
    }

}
