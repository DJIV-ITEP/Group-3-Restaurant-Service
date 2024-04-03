package com.itep.restaurant_service.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class OrderResource {
	// private static final long serialVersionUID = 1L;

	public enum OrderStatus {
		Created, Processing, Paid, Finished, Cancelld
	};

	public Long OrderId;
	public Long RestaurantId;
	public String RestaurantName;
	public Long CustomerId;
	public String CustomerName;
	public Long DriverId;
	public String DriverName;
	public Long AddressId;
	private String locaion;
	public String AddressDesc;
	public LocalDateTime OrderDate;

	public double Total;
	public Long OrderStatusID;
	public OrderStatus OrderStatus;

}
