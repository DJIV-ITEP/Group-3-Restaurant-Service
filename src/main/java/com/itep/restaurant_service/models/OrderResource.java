package com.itep.restaurant_service.models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderResource {
	// private static final long serialVersionUID = 1L;

	public enum OrderStatus {
		Created, Processing, Paid, Finished, Cancelld
	}

	public Long OrderId;
	public Long CustomerId;
	public String CustomerName;
	public Long AddressId;
	public String AddressDesc;
	public String location;
	public LocalDateTime OrderDate;
	public double Total;
	public List<OrderStatusResource> OrderStatus;
	public List<OrderDetails> OrderDetails;

}
