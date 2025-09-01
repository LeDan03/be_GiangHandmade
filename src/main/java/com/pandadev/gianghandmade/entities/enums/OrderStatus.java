package com.pandadev.gianghandmade.entities.enums;

public enum OrderStatus {
    PENDING,//Don moi, cho xu ly
    CONFIRMED,//Da xac nhan
    CANCELLED,
    SHIPPED, //Dang giao hang/da gui hang di
    DELIVERED,//Da giao thanh cong
    PROCESSING,//Dang chuan bi hang
    RETURNED,//Khach tra hang
    REFUNDED//Da hoan tien
}
