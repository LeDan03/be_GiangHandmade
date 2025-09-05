package com.pandadev.gianghandmade.entities.enums;

import lombok.Getter;

@Getter
public enum ProductStatus {
    AVAILABLE("Đang bán"),
    OUT_OF_STOCK("Hết hàng"),
    HIDDEN("Ẩn"),
    HOT("Cháy hàng"),
    SOLD_OUT("Đã hết hàng");

    private final String label;
    ProductStatus() {
        this.label = name();
    }
    ProductStatus(String label) {
        this.label = label;
    }

}
