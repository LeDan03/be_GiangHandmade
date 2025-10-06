package com.pandadev.gianghandmade.repositories;

import com.pandadev.gianghandmade.entities.ShippingFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingFeeRepository extends JpaRepository<ShippingFee, Integer> {
}
