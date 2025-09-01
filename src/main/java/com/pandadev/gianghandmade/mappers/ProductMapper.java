package com.pandadev.gianghandmade.mappers;

import com.pandadev.gianghandmade.entities.Product;
import com.pandadev.gianghandmade.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ImageMapper imageMapper;

    public ProductResponse toResponse(Product product) {
        return  ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .categoryId(product.getCategory().getId())
                .images(imageMapper.toResponses(product.getImages()))
                .soldCount(product.getSoldCount())
                .createdAt(product.getCreatedAt())
                .status(product.getStatus().name())
                .build();
    }

    public List<ProductResponse> toResponses(List<Product> products) {
        return products.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
