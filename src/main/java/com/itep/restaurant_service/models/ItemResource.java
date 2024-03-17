package com.itep.restaurant_service.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ItemResource {
    private long id;
    private String name;
    private double price;

}
