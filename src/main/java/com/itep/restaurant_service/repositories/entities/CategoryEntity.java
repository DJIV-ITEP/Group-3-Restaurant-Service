package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.CategoryResource;
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
@Table(name = "categories",uniqueConstraints={
        @UniqueConstraint(columnNames = {"restaurant_id", "name"})
})
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RestaurantEntity restaurant;

    public CategoryResource toCategoryResource() {
        return CategoryResource
                .builder()
                .id(id)
                .name(name)
                .restaurant(restaurant.getId())
                .build();
    }
}
