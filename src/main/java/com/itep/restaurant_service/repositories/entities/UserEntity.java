package com.itep.restaurant_service.repositories.entities;


//import com.itep.restaurant_service.models.UserResource;
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
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "password")
    private String password;
//    public UserResource toAdminResource() {
//        return UserResource
//                .builder()
//                .username(username)
//                .password(password)
//                .build();
//    }
}
