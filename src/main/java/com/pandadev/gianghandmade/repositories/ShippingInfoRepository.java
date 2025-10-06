package com.pandadev.gianghandmade.repositories;

import com.pandadev.gianghandmade.entities.ShippingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, Integer> {
}
