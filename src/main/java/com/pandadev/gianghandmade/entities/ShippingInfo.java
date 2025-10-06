package com.pandadev.gianghandmade.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @JsonBackReference
    private User user; //1 user co the tao nhieu dia chi nhan hang
}
