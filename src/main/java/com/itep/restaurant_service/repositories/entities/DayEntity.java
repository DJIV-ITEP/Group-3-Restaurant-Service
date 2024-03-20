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
@Table(name = "days")
public class DayEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShiftEntity shift;
    public DayResource toDayResource() {
        return DayResource
                .builder()
                .id(id)
                .name(name)
                .build();
    }

}
