package com.itep.restaurant_service.models;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetails {
    public long ProductId;
    public String ProducName ;
    public String CustomizationNote ;
    public long Quantity;
    public double Price;
    public double Total;
    
}
