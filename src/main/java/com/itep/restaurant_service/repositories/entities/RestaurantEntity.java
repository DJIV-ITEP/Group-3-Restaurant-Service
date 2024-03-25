package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.RestaurantResource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "restaurants")
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

    @OneToOne
    @JoinColumn(name = "owner", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity owner;


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
