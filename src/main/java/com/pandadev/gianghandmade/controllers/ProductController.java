package com.pandadev.gianghandmade.controllers;

import com.pandadev.gianghandmade.requests.ProductRequest;
import com.pandadev.gianghandmade.responses.ApiResponse;
import com.pandadev.gianghandmade.responses.ProductResponse;
import com.pandadev.gianghandmade.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int productId){
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

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteProduct(@RequestParam int productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().body(new ApiResponse("Đã xóa sản phẩm", 200));
    }
}
