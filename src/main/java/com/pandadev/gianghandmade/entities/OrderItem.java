package com.pandadev.gianghandmade.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "id")
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    @JsonBackReference
    private Order order;
}
