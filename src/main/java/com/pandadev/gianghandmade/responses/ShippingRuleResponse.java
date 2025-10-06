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
public class ShippingRuleResponse {

    private int id;
    private String regionType;
    private BigDecimal baseFee;
    private boolean active;
}
