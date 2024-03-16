package com.itep.restaurant_service.repositories.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "days")
public class DayEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;

    DayEntity() {
    }

    public DayEntity(String name) {
        this.name = name;
    }

}
