package com.itep.restaurant_service.repositories.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "restaurants")
public class RestaurantEntity {
    @Id
    @GeneratedValue
    public long id;
    @Column(name = "name", nullable = false)
    public String name;
    @Column(name = "address", nullable = false)
    public String address;
    @Column(name = "location", nullable = false)
    public  String location;
    @Column(name = "status", nullable = false)
    public String status="offline";
    @Column(name = "food", nullable = false)
    public String food;
    @Column(name = "cuisine", nullable = false)
    public String cuisine;
    @Column(name = "username", nullable = false)
    public String username;
    @Column(name = "password", nullable = false)
    public String password;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    Collection<CategoryEntity> categories;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    Collection<ShiftEntity> shifts;

    public RestaurantEntity(){

    }
    

    public RestaurantEntity(String name,
                            String address,
                            String location,
                            String status,
                            String food,
                            String cuisine,
                            String username,
                            String password) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.status = status;
        this.food = food;
        this.cuisine = cuisine;
        this.username = username;
        this.password = password;
    }

}
