package com.itep.restaurant_service.models;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class OrderResource {
    // private static final long serialVersionUID = 1L;



    public String OrderId;
    public long RestaurantId;
    public String RestaurantName;
    public long CustomerId;
    public String CustomerName;
    public long DriverId;
    public String DriverName;
    public long AddressId;
    public String AddressDesc;
    private long OrderStatusID;
    public OrderStatus OrderStatus;
    public double Total;
    public double ItemCount;

    public List<OrderItems> OrderDetails;

}