package com.pandadev.gianghandmade.responses;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private int cartId;
    private int quantity;
    private BigDecimal totalPrice;
    private List<CartItemResponse> items;
}
