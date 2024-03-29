package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.CategoryResource;
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
@Entity
@Table(name = "categories", uniqueConstraints={@UniqueConstraint(columnNames = {"name", "restaurant_id"})})
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
    private List<MenuEntity> menus;

    public CategoryResource toCategoryResource() {
        return CategoryResource
                .builder()
                .id(id)
                .name(name)
                .restaurant(restaurant.getId())
                .build();
    }
}
