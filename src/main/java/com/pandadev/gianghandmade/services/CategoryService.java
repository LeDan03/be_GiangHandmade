package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.entities.Category;
import com.pandadev.gianghandmade.exceptions.BadRequestException;
import com.pandadev.gianghandmade.exceptions.ForbiddenException;
import com.pandadev.gianghandmade.exceptions.NotFoundException;
import com.pandadev.gianghandmade.mappers.CategoryMapper;
import com.pandadev.gianghandmade.repositories.CategoryRepository;
import com.pandadev.gianghandmade.repositories.ProductRepository;
import com.pandadev.gianghandmade.requests.CategoryRequest;
import com.pandadev.gianghandmade.responses.CategoryResponse;
import com.pandadev.gianghandmade.utils.CategoryValidationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryValidationUtil categoryValidationUtil;

    private final ProductRepository productRepository;

    @PostConstruct
    public void init(){
        Category khac = new Category();
        khac.setName("Khác");
        khac.setDescription("Góc nhỏ cho những món đồ không theo khuôn mẫu, vừa đủ làm ấm trái tim");

        Category thuBong = new Category();
        thuBong.setName("Thú bông");
        thuBong.setDescription("Gửi cả trời dịu dàng vào từng sợi len xinh xắn");

        Category anh = new Category();
        anh.setName("Làm theo ảnh");
        anh.setDescription("Chỉ cần một tấm ảnh, Giang Handmade kể tiếp câu chuyện bằng đôi tay.");

        Category datRieng = new Category();
        datRieng.setName("Đặt làm riêng");
        datRieng.setDescription("Bạn nghĩ ra, Giang Handmade tạo nên – một món quà chỉ dành cho riêng bạn");

        Category mocKhoa = new Category();
        mocKhoa.setName("Móc khóa");
        mocKhoa.setDescription("Chút xinh xinh mang theo mỗi ngày, vừa tiện vừa vui");

        if(!categoryRepository.existsByIdNotNull()){
            categoryRepository.saveAll(List.of(khac, datRieng, mocKhoa, thuBong));
        }
    }

    public List<CategoryResponse> getAllCategories(){
        return categoryMapper.toResponses(categoryRepository.findAll());
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest){
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new BadRequestException("Phân loại đã tồn tại");
        }
        categoryValidationUtil.validateCategory(categoryRequest);

        Category category = new Category();
        category.setName(categoryValidationUtil.cleanName(categoryRequest.getName()));
        category.setDescription(categoryValidationUtil.cleanDescription(categoryRequest.getDescription()));
        categoryRepository.save(category);

        return categoryMapper.toResponse(category);
    }

    public CategoryResponse updateCategoryById(int categoryId, CategoryRequest categoryRequest){
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if(categoryOpt.isEmpty()){
            throw new NotFoundException("Phân loại không tồn tại");
        }
        categoryValidationUtil.validateCategory(categoryRequest);
        Category category = categoryOpt.get();
        category.setName(categoryValidationUtil.cleanName(categoryRequest.getName()));
        category.setDescription(categoryValidationUtil.cleanDescription(categoryRequest.getDescription()));

        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }
    public void deleteCategoryById(int categoryId){
        if(productRepository.existsById(categoryId)){
            throw new ForbiddenException("Phân loại đã có sản phẩm, không thể xóa");
        }
        if(!categoryRepository.existsById(categoryId)){
            throw new NotFoundException("Phân loại không tồn tại");
        }
        categoryRepository.deleteById(categoryId);
    }
}
