package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.MenuResource;
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
@Table(name = "menus",uniqueConstraints={
        @UniqueConstraint(columnNames = {"category_id", "name"})
})
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL)
    private List<ItemEntity> items;

    public MenuResource toMenuResource() {
        return MenuResource
                .builder()
                .id(id)
                .name(name)
                .category(category.getId())
                .build();
    }

}
