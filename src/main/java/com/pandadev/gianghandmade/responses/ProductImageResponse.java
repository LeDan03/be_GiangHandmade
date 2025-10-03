package com.pandadev.gianghandmade.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductImageResponse extends ImageResponse{
    private int productId;
}
