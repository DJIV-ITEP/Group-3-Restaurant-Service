package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.MenuResource;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menus",uniqueConstraints={@UniqueConstraint(columnNames = {"name", "category_id"})})
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ItemEntity> items;

    public MenuEntity(String newMenu) {
    }



    public MenuResource toMenuResource() {
        return MenuResource
                .builder()
                .id(id)
                .name(name)
                .category(category.getId())
                .build();
    }

}
