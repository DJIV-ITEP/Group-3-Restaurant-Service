package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.ShiftResource;
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
@Table(name = "shifts",uniqueConstraints={
        @UniqueConstraint(columnNames = {"restaurant_id", "name"})
})
public class ShiftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shift", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DayEntity> days;

    public ShiftResource toShiftResource() {
        return ShiftResource
                .builder()
                .id(id)
                .name(name)
                .build();
    }

}
