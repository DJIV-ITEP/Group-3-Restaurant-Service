package com.itep.restaurant_service.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResource {
    private String username;
    private String password;

}
