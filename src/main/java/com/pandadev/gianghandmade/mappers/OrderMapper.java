package com.pandadev.gianghandmade.mappers;

import com.pandadev.gianghandmade.entities.Order;
import com.pandadev.gianghandmade.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;

    public OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .items(orderItemMapper.toOrderItemResponseList(order.getOrderItems()))
                .paymentMethod(order.getPaymentMethod())
                .totalQuantity(order.getTotalQuantity())
                .userId(order.getUser().getId())
                .build();
    }

    public List<OrderResponse> toOrderResponseList(List<Order> orders) {
        return orders.stream().map(this::toOrderResponse).toList();
    }
}
