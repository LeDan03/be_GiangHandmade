package com.pandadev.gianghandmade.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    private int productId;
    private int quantity;
}
