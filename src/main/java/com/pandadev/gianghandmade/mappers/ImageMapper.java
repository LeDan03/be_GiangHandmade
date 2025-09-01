package com.pandadev.gianghandmade.mappers;

import com.pandadev.gianghandmade.entities.Image;
import com.pandadev.gianghandmade.entities.Product;
import com.pandadev.gianghandmade.repositories.ImageRepository;
import com.pandadev.gianghandmade.requests.ImageRequest;
import com.pandadev.gianghandmade.responses.ImageResponse;
import com.pandadev.gianghandmade.utils.ProductValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImageMapper {
    private final ImageRepository imageRepository;

    public Image toEntity(ImageRequest imageRequest, Product product){
        Image image = new Image();
        image.setSecureUrl(imageRequest.getSecureUrl());
        image.setPublicId(imageRequest.getPublicId());
        image.setProduct(product);
        return image;
    }

    public List<Image> toEntities(List<ImageRequest> imageRequests, Product product){
        return imageRequests.stream()
                .map(imageRequest -> toEntity(imageRequest,product))
                .collect(Collectors.toList());
    }

    public ImageResponse toResponse(Image image){
        return ImageResponse.builder()
                .id(image.getId())
                .secureUrl(image.getSecureUrl())
                .productId(image.getProduct().getId())
                .build();
    }

    public List<ImageResponse> toResponses(List<Image> images){
        return images.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
