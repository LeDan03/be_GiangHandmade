package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.entities.Category;
import com.pandadev.gianghandmade.entities.Image;
import com.pandadev.gianghandmade.entities.Product;
import com.pandadev.gianghandmade.entities.enums.ProductStatus;
import com.pandadev.gianghandmade.exceptions.NotFoundException;
import com.pandadev.gianghandmade.mappers.ImageMapper;
import com.pandadev.gianghandmade.mappers.ProductMapper;
import com.pandadev.gianghandmade.repositories.CategoryRepository;
import com.pandadev.gianghandmade.repositories.ImageRepository;
import com.pandadev.gianghandmade.repositories.ProductRepository;
import com.pandadev.gianghandmade.requests.ProductRequest;
import com.pandadev.gianghandmade.responses.ProductResponse;
import com.pandadev.gianghandmade.utils.ProductValidationUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;

    private final ProductValidationUtil productValidationUtil;

    private final CloudinaryService cloudinaryService;

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public List<ProductResponse> findProducts(String keyword, Integer categoryId, String productStatus) {
        List<Product> products;
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productRepository.search(keyword.trim());
//            logger.info("Search với key: {}, kết quả: {}", keyword, products.get(0).getName());
        } else {
            products = productRepository.findAll();
        }
        //  Lọc theo categoryId nếu có
        if (categoryId != null) {
            products = products.stream()
                    .filter(p -> p.getCategory() != null && p.getCategory().getId() == categoryId)
                    .toList();
        }
        // Lọc theo status nếu có
        if (productStatus != null && !productStatus.isBlank()) {
            ProductStatus statusEnum = ProductStatus.valueOf(productStatus.toUpperCase());
            products = products.stream()
                    .filter(p -> p.getStatus() == statusEnum)
                    .toList();
        }

        return productMapper.toResponses(products);
    }

    public ProductResponse getProductById(int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty())
            throw new NotFoundException("Sản phẩm đã bị xóa");
        return productMapper.toResponse(product.get());
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        Optional<Category> categoryOpt = categoryRepository.findById(productRequest.getCategoryId());
        if (categoryOpt.isEmpty()) {
            throw new NotFoundException("Phân loại không tồn tại");
        }
        productValidationUtil.validateProduct(productRequest);

        Category category = categoryOpt.get();
        Product product = new Product();
        List<Image> images = imageMapper.toEntities(productRequest.getImages(), product);

        product.setName(productValidationUtil.cleanProductName(productRequest.getName()));
        product.setDescription(productValidationUtil.cleanProductDescription(productRequest.getDescription()));
        product.setPrice(productRequest.getPrice());
        product.setCategory(category);
        product.setQuantity(productRequest.getQuantity());
        product.setImages(images);
        product.setStatus(productValidationUtil.getNewProductStatus(product.getQuantity()));
        product.setCreatedAt(Timestamp.from(Instant.now()));

        productRepository.save(product);

        return productMapper.toResponse(product);
    }

    public ProductResponse updateProduct(int productId, ProductRequest productRequest) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new NotFoundException("Sản phẩm đã bị xóa");
        }
        productValidationUtil.validateProduct(productRequest);

        Product product = productOpt.get();

        product.setName(productValidationUtil.cleanProductName(productRequest.getName()));
        product.setDescription(productValidationUtil.cleanProductDescription(productRequest.getDescription()));
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        productRepository.save(product);
        updateProductImages(product, productRequest);
        return productMapper.toResponse(product);
    }

    public void updateProductImages(Product product, ProductRequest productRequest) {
        List<Image> oldImages = product.getImages();
        List<Image> newImages = imageMapper.toEntities(productRequest.getImages(), product);

        List<Image> imagesToDelete = oldImages.stream()
                .filter(oldImg -> newImages.stream()
                        .noneMatch(newImg -> newImg.getPublicId().equals(oldImg.getPublicId())))
                .toList();

        // Xóa ảnh không còn tồn tại trong request
        oldImages.removeIf(oldImg ->
                newImages.stream().noneMatch(newImg -> newImg.getPublicId().equals(oldImg.getPublicId()))
        );

        // Thêm ảnh mới (chỉ thêm nếu chưa tồn tại)
        newImages.forEach(newImg -> {
            boolean exists = oldImages.stream()
                    .anyMatch(oldImg -> oldImg.getPublicId().equals(newImg.getPublicId()));
            if (!exists) {
                oldImages.add(newImg);
            }
        });
        productRepository.save(product);
        imagesToDelete.forEach(img -> {
            try {
                cloudinaryService.delete(img.getPublicId());
            } catch (Exception e) {
                logger.warn("Failed to delete image from Cloudinary: {}", img.getPublicId(), e);
            }
        });

    }

    public void deleteProduct(int productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Sản phẩm cần xóa không tồn tại");
        }
        List<Image> productImage = imageRepository.findByProductId(productId);
        productImage.forEach(image -> {
            cloudinaryService.delete(image.getPublicId());
        });
        productRepository.deleteById(productId);
    }

    public void deleteProductsByIds(List<Integer> productIds) {
        if(productIds.isEmpty()){
            return;
        }
        for(Integer productId : productIds){
            deleteProduct(productId);
        }
    }

    public void changeCategoryByIds(List<Integer> productIds, int categoryId) {
        try {
            productRepository.changeCategoryByIdIn(categoryId, productIds);
        } catch (Exception e) {
            logger.warn("Lỗi khi thay đổi phân loại");
            throw new RuntimeException("Lỗi thay đổi phân loại cho sản phẩm: " + e.getMessage());
        }
    }
}
