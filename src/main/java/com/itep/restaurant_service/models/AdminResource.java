package com.itep.restaurant_service.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AdminResource {
    private String username;
    private String password;

}
