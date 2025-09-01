package com.pandadev.gianghandmade.requests;

import com.pandadev.gianghandmade.responses.ImageResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private List<ImageRequest> images;
    private int categoryId;
}
