package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.ShiftResource;
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
@Table(name = "shifts",uniqueConstraints={
        @UniqueConstraint(columnNames = {"restaurant_id", "name"})
})
public class ShiftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RestaurantEntity restaurant;
    public ShiftResource toShiftResource() {
        return ShiftResource
                .builder()
                .id(id)
                .name(name)
                .build();
    }

}
