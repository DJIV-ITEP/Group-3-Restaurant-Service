package com.itep.restaurant_service.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RestaurantResource {
    public long id;
    public String name;
    public String address;
    public  String location;
    public String status="offline";
    public String food;
    public String cuisine;
//    Hide These information
//    public String username;
//    public String password;
}
