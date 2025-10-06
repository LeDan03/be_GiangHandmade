package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.entities.ShippingInfo;
import com.pandadev.gianghandmade.entities.ShippingRule;
import com.pandadev.gianghandmade.entities.enums.RegionType;
import com.pandadev.gianghandmade.exceptions.NotFoundException;
import com.pandadev.gianghandmade.repositories.ShippingInfoRepository;
import com.pandadev.gianghandmade.repositories.ShippingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ShippingInfoRepository shippingInfoRepository;
    private final ShippingRuleRepository shippingRuleRepository;

    public ShippingInfo getShippingInfoById(int id){
        Optional<ShippingInfo> shippingInfoOpt = shippingInfoRepository.findById(id);
        if (shippingInfoOpt.isEmpty()) {
            throw new NotFoundException("Không tìm thấy thông tin đặt hàng");
        }
        return shippingInfoOpt.get();
    }

    public BigDecimal getFinalShippingFee(BigDecimal extraFee, RegionType regionType) {
        Optional<ShippingRule> shippingRuleOption = shippingRuleRepository.findByRegionType(regionType);
        if (shippingRuleOption.isEmpty()) {
            throw new NotFoundException("Không tìm thấy shipping rule");
        }
        ShippingRule shippingRule = shippingRuleOption.get();
        if(extraFee.compareTo(BigDecimal.ZERO) == 0){
            return shippingRule.getBaseFee();
        }
        else return shippingRule.getBaseFee().add(extraFee);
    }
}
