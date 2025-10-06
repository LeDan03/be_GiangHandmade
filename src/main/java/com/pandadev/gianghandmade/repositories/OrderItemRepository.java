package com.pandadev.gianghandmade.repositories;

import com.pandadev.gianghandmade.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
