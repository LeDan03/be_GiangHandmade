package com.pandadev.gianghandmade.repositories;

import com.pandadev.gianghandmade.entities.ShippingRule;
import com.pandadev.gianghandmade.entities.enums.RegionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippingRuleRepository extends JpaRepository<ShippingRule, Integer> {
    Optional<ShippingRule> findByRegionType(RegionType regionType);
}
