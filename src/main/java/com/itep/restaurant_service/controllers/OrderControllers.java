package com.itep.restaurant_service.controllers;

import com.itep.restaurant_service.models.OrderResource;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.impl.OrderServiceImpl;
import com.itep.restaurant_service.services.impl.errorsHandels.RestaurantNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.UserNotOwnerOfRestaurantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
public class OrderControllers {

    private final OrderServiceImpl ordertService;

    public OrderControllers(OrderServiceImpl ordertService) {
        this.ordertService = ordertService;
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/accapt")
    public ResponseEntity<Object> accaptOrder(@PathVariable long restaurantId, @PathVariable String orderId){
        try{
            return new ResponseEntity<>(Map.of(
                    "code",200,
                    "Message",ordertService.accaptOrder(restaurantId,orderId)), HttpStatus.OK);
        }
        catch (RestaurantNotFoundException ee){
            return new ResponseEntity<>(Map.of("code",404, "message",ee.getMessage()), HttpStatus.NOT_FOUND);
        }
        catch (UserNotOwnerOfRestaurantException ex){
            return new ResponseEntity<>(Map.of("code",403, "message",ex.getMessage()), HttpStatus.FORBIDDEN);
        }
        catch (Exception e){
            return new ResponseEntity<>(Map.of("code",400, "message",e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/reject")
    public ResponseEntity<Object> rejectOrder(@PathVariable long restaurantId, @PathVariable String orderId){
        try {
            return new ResponseEntity<>(Map.of(
                    "code",200,
                    "Message",ordertService.cancelOrder(restaurantId,orderId)), HttpStatus.OK);
        }
        catch (RestaurantNotFoundException ee){
            return new ResponseEntity<>(Map.of("code",404, "message",ee.getMessage()), HttpStatus.NOT_FOUND);
        }
        catch (UserNotOwnerOfRestaurantException ex){
            return new ResponseEntity<>(Map.of("code",403, "message",ex.getMessage()), HttpStatus.FORBIDDEN);
        }
        catch (Exception e){
            return new ResponseEntity<>(Map.of("code",400, "message",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/markReady")
    public ResponseEntity<Object> markOrderAsReady(@PathVariable long restaurantId, @PathVariable String orderId) {
        try{
            return new ResponseEntity<>(Map.of(
                    "code",200,
                    "Message",ordertService.markOrderAsReady(restaurantId,orderId)), HttpStatus.OK);
        }
        catch (RestaurantNotFoundException ee){
            return new ResponseEntity<>(Map.of("code",404, "message",ee.getMessage()), HttpStatus.NOT_FOUND);
        }
        catch (UserNotOwnerOfRestaurantException ex){
            return new ResponseEntity<>(Map.of("code",403, "message",ex.getMessage()), HttpStatus.FORBIDDEN);
        }
        catch (Exception e){
            return new ResponseEntity<>(Map.of("code",400, "message",e.getMessage()), HttpStatus.BAD_REQUEST);
        }

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

    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @GetMapping("/restaurants/{restaurantId}/incomingOrders")
    public ResponseEntity<Object> getIncomingOrders(@PathVariable long restaurantId) {

        try{

            List<OrderResource> result = ordertService.getIncomingOrders(restaurantId);
            return new ResponseEntity<>(Map.of(
                    "code",200,
                    "data",Map.of("items",ordertService.getIncomingOrders(restaurantId))), HttpStatus.OK);
        }
        catch (RestaurantNotFoundException ee){
            return new ResponseEntity<>(Map.of("code",404, "message",ee.getMessage()), HttpStatus.NOT_FOUND);
        }
        catch (UserNotOwnerOfRestaurantException ex){
            return new ResponseEntity<>(Map.of("code",403, "message",ex.getMessage()), HttpStatus.FORBIDDEN);
        }
        catch (Exception e){
            return new ResponseEntity<>(Map.of("code",400, "message",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        // // استرجع الطلبات الواردة من قاعدة البيانات للمطعم المحدد
        // // قم بتنفيذ الأكواد اللازمة هنا
        // List<OrderResource> incomingOrders = // قم بجلب البيانات من قاعدة البيانات

        // return ResponseEntity.ok(incomingOrders);
    }



}
