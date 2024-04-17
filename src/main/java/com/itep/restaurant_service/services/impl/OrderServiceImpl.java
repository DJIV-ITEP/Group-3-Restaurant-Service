package com.itep.restaurant_service.services.impl;

import com.itep.restaurant_service.component.AcceptOrderClient;
import com.itep.restaurant_service.component.GetOrderClient;
import com.itep.restaurant_service.models.OrderResource;
import com.itep.restaurant_service.models.OrderStatus;
import com.itep.restaurant_service.repositories.RestaurantRepository;
import com.itep.restaurant_service.repositories.entities.RestaurantEntity;
import com.itep.restaurant_service.services.OrderService;
import com.itep.restaurant_service.services.impl.errorsHandels.RestaurantNotFoundException;
import com.itep.restaurant_service.services.impl.errorsHandels.UserNotOwnerOfRestaurantException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {
    final private RestaurantRepository restaurantRepository;
//    @Autowired
    private final AcceptOrderClient acceptOrderClient;
//    @Autowired
    private final GetOrderClient getOrderClient;

    @Autowired
    public OrderServiceImpl(RestaurantRepository restaurantRepository, AcceptOrderClient acceptOrderClient, GetOrderClient getOrderClient) {
        this.restaurantRepository = restaurantRepository;

        this.acceptOrderClient = acceptOrderClient;
        this.getOrderClient = getOrderClient;
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
    public List<OrderResource> getIncomingOrders(long restaurantId) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
        if(RestaurantUtils.isRestaurantOwner(restaurant, SecurityContextHolder.getContext().getAuthentication().getName())){
            try{
                return getOrderClient.getOrderList(restaurantId);
            }
            catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }
        else{
            throw new UserNotOwnerOfRestaurantException();
        }


    }
    @Override
    public ResponseEntity<String> markOrderAsReady(long restaurantId, String orderId) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
        if(RestaurantUtils.isRestaurantOwner(restaurant, SecurityContextHolder.getContext().getAuthentication().getName())){
            try {
                long statusId = 0;
                List<OrderStatus> status = getOrderClient.getStatus();
                for (OrderStatus orderStatus : status) {
                    if (Objects.equals(orderStatus.getName(), "Ready for Pick up")) {
                        statusId = orderStatus.getId();
                    }
                }
                return acceptOrderClient.changeOrderStatus(orderId, statusId);
            }
            catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }
        else{
            throw new UserNotOwnerOfRestaurantException();
        }
        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'markOrderAsReady'");

    }









    @Override
    public ResponseEntity<String> cancelOrder(long restaurantId, String orderId) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
        if(RestaurantUtils.isRestaurantOwner(restaurant, SecurityContextHolder.getContext().getAuthentication().getName())){
            try {
                return acceptOrderClient.acceptOrder(orderId, false);
            }
            catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }
        else{
            throw new UserNotOwnerOfRestaurantException();
        }



    }

    @Override
    public ResponseEntity<String> accaptOrder(long restaurantId,String orderId) throws Exception {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
        if(RestaurantUtils.isRestaurantOwner(restaurant, SecurityContextHolder.getContext().getAuthentication().getName())){
            try{
                return acceptOrderClient.acceptOrder(orderId, true);
            }
            catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }
        else{
            throw new UserNotOwnerOfRestaurantException();
        }


    }

//    @Override
//    public OrderResource getOrderByRestaurantId(long restaurantId) throws Exception {
//        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
//                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
//        if(RestaurantUtils.isRestaurantOwner(restaurant, SecurityContextHolder.getContext().getAuthentication().getName())){
//            try{
//                throw new UnsupportedOperationException("Unimplemented method 'getOrderByRestaurantId'");
//            }
//            catch (Exception e){
//                throw new Exception(e.getMessage());
//            }
//        }
//        else{
//            throw new UserNotOwnerOfRestaurantException();
//        }
//        // TODO Auto-generated method stub
//
//    }




}
