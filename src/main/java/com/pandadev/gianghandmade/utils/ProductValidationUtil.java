package com.pandadev.gianghandmade.utils;

import com.pandadev.gianghandmade.entities.enums.ProductStatus;
import com.pandadev.gianghandmade.exceptions.BadRequestException;
import com.pandadev.gianghandmade.requests.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ProductValidationUtil {

    public void validateProduct(ProductRequest productRequest) {
        validateName(productRequest.getName());
        validateDescription(productRequest.getDescription());
        validatePrice(productRequest.getPrice());
        validateQuantity(productRequest.getQuantity());
        validateImageCount(productRequest.getImages().size());
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Giá sản phẩm phải lớn hơn 0");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new BadRequestException("Số lượng sản phẩm không thể âm");
        }
    }

    private void validateName(String productName) {
        if (productName == null || productName.isEmpty()) {
            throw new BadRequestException("Tên sản phẩm không thể để trống");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new BadRequestException("Hãy mô tả sản phẩm nhé");
        }
    }

    public String cleanProductName(String productName) {
        if (productName == null) return null;

        // Xóa khoảng trắng thừa
        productName = productName.trim()
                .replaceAll("\\s+", " ") // nhiều space -> 1 space
                .replaceAll("[^\\p{L}\\p{N}\\s]", ""); // giữ lại chữ (mọi ngôn ngữ), số và khoảng trắng

        // Viết hoa chữ cái đầu mỗi từ (cả tiếng Việt có dấu)
        Pattern pattern = Pattern.compile("\\b\\p{L}");
        Matcher matcher = pattern.matcher(productName.toLowerCase());

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group().toUpperCase());
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public String cleanProductDescription(String description) {
        if (description == null || description.isEmpty()) {
            return description;
        }
        //chuẩn hóa khoảng trắng & dấu câu
        String result = description.trim()
                .replaceAll("\\s+", " ")        // gộp nhiều khoảng trắng thành 1
                .replaceAll("\\s([.,;?!])", "$1") // bỏ khoảng trắng trước dấu câu
                .replaceAll("([,;?!])([^\\s])", "$1 $2"); // thêm khoảng trắng sau dấu câu nếu thiếu

        result = result.toLowerCase();
        //viết hoa chữ cái đầu tiên mỗi câu
        StringBuilder sb = new StringBuilder(result.length());
        boolean capitalizeNext = true;
        for (char c : result.toCharArray()) {
            if (capitalizeNext && Character.isLetter(c)) {
                sb.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                sb.append(c);
            }
            if (c == '.' || c == '!' || c == '?') {
                capitalizeNext = true;
            }
        }

        return sb.toString();
    }

    public void validateImageCount(int count) {
        if (count > 3) {
            throw new BadRequestException("Chỉ có thể chọn tối đa 3 ảnh cho một sản phẩm");
        }
    }

    public ProductStatus getNewProductStatus(int quantity) {
        if (quantity == 0) {
            return ProductStatus.OUT_OF_STOCK;
        }
        return ProductStatus.AVAILABLE;
    }
}
