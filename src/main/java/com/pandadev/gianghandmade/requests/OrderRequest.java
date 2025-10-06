package com.pandadev.gianghandmade.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private int customerId;
    private String paymentMethod;
    private String regionType;
    private int shippingInfoId;
    private List<OrderItemRequest> orderItems;
    private BigDecimal extraFee;

}
