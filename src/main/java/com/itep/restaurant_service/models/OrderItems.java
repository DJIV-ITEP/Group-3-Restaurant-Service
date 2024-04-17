package com.itep.restaurant_service.models;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderItems {
    public long ProductId;
    public String ProductName ;
    public long Quantity;
    public double Price;
    public String CustomizationNote ;
    public double Total;

}
