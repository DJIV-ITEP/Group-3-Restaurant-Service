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
@Table(name = "menus")
public class MenuEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CategoryEntity category;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ItemEntity> items;

    public MenuEntity(String newMenu) {
    }

    public MenuResource toMenuResource() {
        return MenuResource
                .builder()
                .id(id)
                .name(name)
                .build();
    }

}
