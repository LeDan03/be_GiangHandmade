package com.pandadev.gianghandmade.entities.enums;

public enum OrderStatus {
    PENDING,//Don moi, cho xu ly
    CONFIRMED,//Da xac nhan
    CANCELLED, //Huy don khong can hoan tien
    SHIPPED, //Dang giao hang/da gui hang di, khong cho cancel
    DELIVERED,//Da giao thanh cong
    PROCESSING,//Dang chuan bi hang
    RETURNED,//Da tra hang
    RETURN_REQUESTED,//Khach yeu cau tra hang
    REFUNDED,//Da hoan tien
    REFUND_PENDING
}
