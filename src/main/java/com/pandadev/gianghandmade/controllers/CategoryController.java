package com.pandadev.gianghandmade.controllers;

import com.pandadev.gianghandmade.requests.CategoryRequest;
import com.pandadev.gianghandmade.responses.ApiResponse;
import com.pandadev.gianghandmade.responses.CategoryResponse;
import com.pandadev.gianghandmade.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        return ResponseEntity.ok().body(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryRequest));
    }

    @PutMapping
    public ResponseEntity<CategoryResponse> updateCategory(@RequestParam int categoryId,@RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.ok().body(categoryService.updateCategoryById(categoryId, categoryRequest));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam int categoryId){
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok().body(new ApiResponse("Đã xóa phân loại",200));
    }
}
