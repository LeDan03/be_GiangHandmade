package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.entities.OrderItem;
import com.pandadev.gianghandmade.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public List<OrderItem> findByIds(List<Long> ids)
    {
        return orderItemRepository.findAllById(ids);
    }

    public BigDecimal getTotalPrice(List<OrderItem> orderItems)
    {
        return orderItems.stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalQuantity(List<OrderItem> orderItems)
    {
        return  orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
    }
}
