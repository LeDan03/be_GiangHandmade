package com.pandadev.gianghandmade.controllers;

import com.pandadev.gianghandmade.entities.enums.ProductStatus;
import com.pandadev.gianghandmade.requests.ProductRequest;
import com.pandadev.gianghandmade.responses.ApiResponse;
import com.pandadev.gianghandmade.responses.ProductResponse;
import com.pandadev.gianghandmade.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam(value = "keyword", required = false) String keyword,
                                                             @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                             @RequestParam(value = "productStatus", required = false) String productStatsus) {
        return ResponseEntity.ok().body(productService.findProducts(keyword, categoryId, productStatsus));
    }

    @GetMapping(value = "/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int productId) {
        return ResponseEntity.ok().body(productService.getProductById(productId));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequest));
    }

    @PutMapping
    public ResponseEntity<ProductResponse> updateProduct(@RequestParam int productId,
                                                         @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.updateProduct(productId, productRequest));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().body(new ApiResponse("Đã xóa sản phẩm", 200));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteProductsByIds(@RequestBody List<Integer> productIds) {
        productService.deleteProductsByIds(productIds);
        return ResponseEntity.ok().body(new ApiResponse("Đã xóa các sản phẩm được chọn", 200));
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<Map<String, String>>> getAllStatuses() {
        return ResponseEntity.ok().body(
                Arrays.stream(ProductStatus.values())
                        .map(status -> Map.of("value", status.name(), "label", status.getLabel()))
                        .collect(Collectors.toList())
        );
    }

    @PutMapping(value = "/{categoryId}")
    public ResponseEntity<ApiResponse> changeProductsCategory(@RequestBody List<Integer> productIds, @PathVariable int categoryId) {
        productService.changeCategoryByIds(productIds, categoryId);
        return ResponseEntity.ok().body(new ApiResponse("Đã thay đổi phân loại cho danh sách", 200));
    }

}
