package com.itep.restaurant_service.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.itep.restaurant_service.models.OrderResource;
import com.itep.restaurant_service.services.impl.OrderServiceImpl;

public class OrderControllers {

    private final OrderServiceImpl ordertService;

    public OrderControllers(OrderServiceImpl ordertService) {
        this.ordertService = ordertService;
    }

    @PostMapping("/{restaurantId}/orders/{orderId}/markReady")
    public String markOrderAsReady(@PathVariable Long restaurantId, @PathVariable Long orderId) {
        return ordertService.markOrderAsReady(restaurantId, orderId);
        // // قم بتحديث حالة الطلب لتكون جاهزة للاستلام
        // Optional<OrderResource> optionalOrder =
        // ordertService.findByOrderRestaurantIdAndId(restaurantId, orderId);
        // if (optionalOrder.isPresent()) {
        // OrderResource order = optionalOrder.get();

        // // قم بتحديث حالة الطلب لتكون جاهزة للاستلام
        // order.setStatus("READY");

        // // قم بحفظ التغييرات في قاعدة البيانات
        // ordertService.save(order);
        // return ResponseEntity.ok("تم تحديث حالة الطلب بنجاح");
        // }
    }

    @GetMapping("/{restaurantId}/incomingOrders")
    public List<OrderResource> getIncomingOrders(@PathVariable Long restaurantId) {
        return ordertService.getIncomingOrders(restaurantId);
        // // استرجع الطلبات الواردة من قاعدة البيانات للمطعم المحدد
        // // قم بتنفيذ الأكواد اللازمة هنا
        // List<OrderResource> incomingOrders = // قم بجلب البيانات من قاعدة البيانات

        // return ResponseEntity.ok(incomingOrders);
    }

    @GetMapping("/restaurants/orders/{orderId}")
    public String cancelOrder(@PathVariable Long orderId) {
        return ordertService.cancelOrder(orderId);
        // // Get the order from the orderRepository
        // OrderResource order = ordertService.getOrderById(orderId);

        // // Check if the order exists and belongs to the specified restaurant
        // if (order == null || !order.getRestaurantId().equals(restaurantId)) {
        // return ResponseEntity.notFound().build();
        // }

        // // Delete the order from the orderRepository
        // ordertService.deleteOrder(orderId);

        // return ResponseEntity.ok("Order cancelled successfully");
    }

}
