package com.pandadev.gianghandmade.responses;

import com.pandadev.gianghandmade.entities.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderResponse {
    private long id;
    private int totalQuantity;
    private BigDecimal totalPrice;
    private PaymentMethod paymentMethod;
    private int userId;
    private List<OrderItemResponse> items;

    private Timestamp createdAt;
    private Timestamp estimatedDeliveryDate;
    private Timestamp actualDeliveryDate;
    private int shippingFeeId;

    private int shippingInfoId;

}
