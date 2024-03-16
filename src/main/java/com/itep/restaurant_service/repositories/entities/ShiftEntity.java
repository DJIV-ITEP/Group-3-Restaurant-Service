package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collection;

@Entity
@Table(name = "shifts")
public class ShiftEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<DayEntity> days;

    ShiftEntity() {
    }

    public ShiftEntity(String name) {
        this.name = name;
    }

}
