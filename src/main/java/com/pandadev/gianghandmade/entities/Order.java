package com.pandadev.gianghandmade.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pandadev.gianghandmade.entities.enums.OrderStatus;
import com.pandadev.gianghandmade.entities.enums.PaymentMethod;
import com.pandadev.gianghandmade.entities.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int totalQuantity;

    @Column
    private BigDecimal totalPrice;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp paidAt;

    @Column
    private Timestamp estimatedDeliveryDate;//Ngày giao dự kien

    @Column
    private Timestamp actualDeliveryDate;//Ngày giao thực tế

    @Column
    private String recipientName;

    @Column
    private String phoneNumber;

    @Column
    private String streetAddress;

    @Column
    private String ward;

    @Column
    private String district;

    @Column
    private String province;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private BigDecimal shippingFeeAmount;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "order",  cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;
}
