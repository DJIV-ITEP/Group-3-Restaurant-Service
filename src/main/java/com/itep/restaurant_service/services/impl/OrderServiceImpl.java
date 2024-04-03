package com.itep.restaurant_service.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.itep.restaurant_service.models.OrderResource;
import com.itep.restaurant_service.models.OrderResource.OrderStatus;
import com.itep.restaurant_service.repositories.OrderRepository;
import com.itep.restaurant_service.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // public void processOrder(long IdOrder) {
    // // محاكاة معالجة الطلب
    // System.out.println("معالجة الطلب: " + IdOrder);
    // try {
    // Thread.sleep(5000); // محاكاة وقت تحضير الطعام
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }

    // // إرسال تحديث إلى سيرفر الطلبات بأن الطعام جاهز
    // publishUpdateOrderStatus(IdOrder,OrderStatus.Finished );
    // // }
    // @Override
    // public void publishUpdateOrderStatus(long IdOrder, OrderStatus NameStatus) {
    // // إرسال تحديث بواسطة RabbitTemplate إلى سيرفر الطلبات
    // // String exchange = "order.exchange";
    // String routingKey = String.valueOf(IdOrder);

    // orderMessageTemplate.convertAndSend(routingKey,NameStatus);

    // System.out.println("تم إرسال تحديث لسيرفر الطلبات - الطلب: " + routingKey + "
    // الحالة: " + NameStatus);
    // }

    // @Override
    // public Optional<OrderResource> getOrderByRestaurantId(long restaurantId) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getOrderByRestaurantId'");
    // }

    // @Override
    // public void publish(OrderResource order) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method 'publish'");
    // }

    @Override
    public String markOrderAsReady(Long restaurantId, Long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markOrderAsReady'");
    }

    @Override
    public List<OrderResource> getIncomingOrders(Long restaurantId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIncomingOrders'");
    }

    @Override
    public String cancelOrder(Long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelOrder'");
    }

    @Override
    public Optional<OrderResource> getOrderByRestaurantId(long restaurantId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderByRestaurantId'");
    }

    @Override
    public void publishUpdateOrderStatus(long IdOrder, OrderStatus NameStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'publishUpdateOrderStatus'");
    }
}