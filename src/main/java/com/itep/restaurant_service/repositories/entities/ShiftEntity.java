package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "shifts")
public class ShiftEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "shift_id")
    private Collection<DayEntity> days;

    ShiftEntity() {
    }

    public ShiftEntity(String name) {
        this.name = name;
    }

}
