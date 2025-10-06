package com.pandadev.gianghandmade.mappers;

import com.pandadev.gianghandmade.entities.Order;
import com.pandadev.gianghandmade.entities.OrderItem;
import com.pandadev.gianghandmade.entities.Product;
import com.pandadev.gianghandmade.repositories.OrderItemRepository;
import com.pandadev.gianghandmade.repositories.ProductRepository;
import com.pandadev.gianghandmade.requests.OrderItemRequest;
import com.pandadev.gianghandmade.responses.OrderItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    private  final OrderItemRepository orderItemRepository;
    private final ProductRepository  productRepository;

    public OrderItem toOrderItem(OrderItemRequest request, Order order)  {
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty()){
            throw new RuntimeException("Product not found");
        }
        Product product = productOpt.get();
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(request.getQuantity());
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);
        return orderItem;
    }

    public List<OrderItem> toOrderItems(List<OrderItemRequest> requests, Order order) {
        return requests.stream().map(item-> this.toOrderItem(item, order)).toList();
    }

    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .productId(orderItem.getProduct().getId())
                .orderId(orderItem.getOrder().getId())
                .build();
    }

    public List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItems) {
        return orderItems.stream().map(this::toOrderItemResponse).toList();
    }
}
