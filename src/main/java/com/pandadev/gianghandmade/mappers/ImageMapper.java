package com.pandadev.gianghandmade.mappers;

import com.pandadev.gianghandmade.entities.*;
import com.pandadev.gianghandmade.requests.ImageRequest;
import com.pandadev.gianghandmade.responses.AvatarResponse;
import com.pandadev.gianghandmade.responses.ImageResponse;
import com.pandadev.gianghandmade.responses.ProductImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageMapper {

    // =====================
    // Request -> Entity
    // =====================

    public Image toImageEntity(ImageRequest request) {
        return Image.builder()
                .secureUrl(request.getSecureUrl())
                .publicId(request.getPublicId())
                .build();
    }

    public List<Image> toImageEntities(List<ImageRequest> requests) {
        return requests.stream()
                .map(this::toImageEntity)
                .toList();
    }

    public ProductImage toProductImageEntity(ImageRequest request, Product product) {
        return ProductImage.builder()
                .secureUrl(request.getSecureUrl())
                .publicId(request.getPublicId())
                .product(product)
                .build();
    }

    public List<ProductImage> toProductImageEntities(List<ImageRequest> requests, Product product) {
        return requests.stream()
                .map(req -> toProductImageEntity(req, product))
                .toList();
    }

    public Avatar toAvatarEntity(ImageRequest request, User user) {
        return Avatar.builder()
                .secureUrl(request.getSecureUrl())
                .publicId(request.getPublicId())
                .defaultAvatar(false)
                .user(user)
                .build();
    }
    // =====================
    // Entity -> Response
    // =====================

    public ImageResponse toImageResponse(Image image) {
        if (image == null) return null;

        return ImageResponse.builder()
                .id(image.getId())
                .secureUrl(image.getSecureUrl())
                .build();
    }

    public List<ImageResponse> toImageResponses(List<Image> images) {
        return images.stream()
                .map(this::toImageResponse)
                .toList();
    }

    public ProductImageResponse toProductImageResponse(ProductImage productImage) {
        if (productImage == null) return null;

        return ProductImageResponse.builder()
                .id(productImage.getId())
                .secureUrl(productImage.getSecureUrl())
                .productId(productImage.getProduct() != null ? productImage.getProduct().getId() : 0)
                .build();
    }

    public List<ProductImageResponse> toProductImageResponses(List<ProductImage> productImages) {
        return productImages.stream()
                .map(this::toProductImageResponse)
                .toList();
    }

    public AvatarResponse toAvatarResponse(Image image, User user, boolean defaultAvatar) {
        if (image == null || user == null) return null;

        return AvatarResponse.builder()
                .id(image.getId())
                .secureUrl(image.getSecureUrl())
                .userId(user.getId())
                .defaultAvatar(defaultAvatar)
                .build();
    }
    public AvatarResponse toAvatarResponse(Avatar avatar) {
        return AvatarResponse.builder()
                .id(avatar.getId())
                .secureUrl(avatar.getSecureUrl())
                .userId(avatar.getUser().getId())
                .build();
    }
}
