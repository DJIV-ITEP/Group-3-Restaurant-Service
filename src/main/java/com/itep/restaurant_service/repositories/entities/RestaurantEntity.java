package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.RestaurantResource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "restaurants")
@Table(name = "restaurants")
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "owner", nullable = false)
    private UserEntity owner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryEntity> categories;

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
                .build();
    }

}
