package com.pandadev.gianghandmade.entities.enums;

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

    public String getLabel() {
        return label;
    }
}
