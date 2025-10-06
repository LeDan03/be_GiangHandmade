package com.pandadev.gianghandmade.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private long id;
    private long orderId;
    private int productId;

    private int quantity;
    private String note;
}
