package com.itep.restaurant_service.repositories.entities;


import com.itep.restaurant_service.models.AdminResource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "admins")
public class AdminEntity {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    public AdminResource toAdminResource() {
        return AdminResource
                .builder()
                .username(username)
                .password(password)
                .build();
    }
}
