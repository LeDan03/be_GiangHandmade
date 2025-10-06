package com.pandadev.gianghandmade.entities;

import com.pandadev.gianghandmade.entities.enums.RegionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private RegionType regionType;

    @Column
    private BigDecimal baseFee;

    @Column
    private boolean active;
}
