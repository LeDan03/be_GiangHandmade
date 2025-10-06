package com.pandadev.gianghandmade.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingFeeResponse {
    private int id;
    private BigDecimal extraFee;
    private String note;
    private BigDecimal finalFee;
    private int shippingRuleId;
}
