package com.pandadev.gianghandmade.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private BigDecimal finalFee;

    @ManyToOne
    @JoinColumn(name = "shippingRuleId", referencedColumnName = "id")
    private ShippingRule shippingRule;
}
