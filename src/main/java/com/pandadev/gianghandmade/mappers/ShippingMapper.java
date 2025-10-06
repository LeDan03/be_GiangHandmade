package com.pandadev.gianghandmade.mappers;

import com.pandadev.gianghandmade.entities.ShippingFee;
import com.pandadev.gianghandmade.entities.ShippingRule;
import com.pandadev.gianghandmade.responses.ShippingFeeResponse;
import com.pandadev.gianghandmade.responses.ShippingRuleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShippingMapper { //map car shipping fee va shipping rule

    public ShippingRuleResponse toShippingRuleResponse(ShippingRule shippingRule) {
        return ShippingRuleResponse.builder()
                .id(shippingRule.getId())
                .active(shippingRule.isActive())
                .baseFee(shippingRule.getBaseFee())
                .regionType(shippingRule.getRegionType().name())
                .build();
    }

    public List<ShippingRuleResponse> toShippingRuleResponses(List<ShippingRule> shippingRules) {
        return  shippingRules.stream().map(this::toShippingRuleResponse).toList();
    }

    public ShippingFeeResponse toShippingFeeResponse(ShippingFee shippingFee) {
        return ShippingFeeResponse.builder()
                .id(shippingFee.getId())
                .finalFee(shippingFee.getFinalFee())
                .shippingRuleId(shippingFee.getId())
                .build();
    }
}
