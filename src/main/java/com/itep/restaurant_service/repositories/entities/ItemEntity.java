package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.DayResource;
import com.itep.restaurant_service.models.ItemResource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class ItemEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MenuEntity menu;


    public ItemResource toItemResource() {
        return ItemResource
                .builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }

}
