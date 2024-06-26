package com.itep.restaurant_service.repositories.entities;


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
@Table(name = "items",uniqueConstraints={@UniqueConstraint(columnNames = {"name", "menu_id"})})
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "quantity", nullable = false)
    private double quantity;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuEntity menu;


    public ItemResource toItemResource() {
        return ItemResource
                .builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .quantity(quantity)
                .menu(menu.getId())
                .build();
    }

}
