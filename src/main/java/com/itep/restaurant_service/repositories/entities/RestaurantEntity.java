package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.RestaurantResource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collection;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class RestaurantEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "location", nullable = false)
    private  String location;
    @Column(name = "status", nullable = false)
    private String status="offline";
    @Column(name = "food", nullable = false)
    private String food;
    @Column(name = "cuisine", nullable = false)
    private String cuisine;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private AdminEntity added_by;


    public RestaurantResource toRestaurantResource() {
        return RestaurantResource
                .builder()
                .id(id)
                .name(name)
                .address(address)
                .location(location)
                .status(status)
                .food(food)
                .cuisine(cuisine)
                .username(username)
                .password(password)
                .build();
    }

}
