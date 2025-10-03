package com.pandadev.gianghandmade.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartItemResponse {
    private long id;
    private int productId;
    private int quantity;
    private BigDecimal priceSnapshot;
}
