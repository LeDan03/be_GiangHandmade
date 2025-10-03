package com.pandadev.gianghandmade.responses;

import com.pandadev.gianghandmade.entities.Category;
import com.pandadev.gianghandmade.entities.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductResponse {

    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private List<ProductImageResponse> images;
    private int categoryId;
    private int soldCount;
    private Timestamp createdAt;
    private String status;

    private int rating;
    private int reviews;
}
