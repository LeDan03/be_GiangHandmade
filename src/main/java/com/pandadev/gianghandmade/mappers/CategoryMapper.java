package com.pandadev.gianghandmade.mappers;

import com.pandadev.gianghandmade.entities.Category;
import com.pandadev.gianghandmade.responses.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ProductMapper productMapper;

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .products(productMapper.toResponses(category.getProducts()))
                .build();
    }
    public List<CategoryResponse> toResponses(List<Category> categories) {
        return categories.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
