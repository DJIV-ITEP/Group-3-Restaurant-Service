package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.DayResource;
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
@Table(name = "days",uniqueConstraints={
        @UniqueConstraint(columnNames = {"shift_id", "name"})
})
public class DayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shift_id", nullable = false)
    private ShiftEntity shift;
    public DayResource toDayResource() {
        return DayResource
                .builder()
                .id(id)
                .name(name)
                .build();
    }

}
