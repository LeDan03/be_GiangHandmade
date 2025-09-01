package com.pandadev.gianghandmade.utils;

import com.pandadev.gianghandmade.exceptions.BadRequestException;
import com.pandadev.gianghandmade.requests.CategoryRequest;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CategoryValidationUtil {

    public void validateCategory(CategoryRequest categoryRequest) {
        validateName(categoryRequest.getName());
        validateDescription(categoryRequest.getDescription());
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Tên phân loại không thể để trống");
        }
        String[] words = name.trim().split("\\s+");
        if (words.length < 3) {
            throw new BadRequestException("Tên phân loại quá ngắn");
        }
        if (words.length > 8 || name.length() > 50) {
            throw new BadRequestException("Tên phân loại quá dài");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new BadRequestException("Hãy viết mô tả ngắn cho phân loại nhé! VD: Quà tặng bạn gái!");
        }
        String[] words = description.trim().split("\\s+");
        if (words.length > 30 || description.length() > 200) {
            throw new BadRequestException("Mô tả quá dài rồi, bớt xíu nha");
        }
    }

    public String cleanName(String name) {
        if (name == null) return null;

        // Giữ Unicode (tiếng Việt có dấu), loại bỏ ký tự đặc biệt
        String cleanName = name.trim()
                .replaceAll("\\s+", " ")
                .replaceAll("[^\\p{L}\\p{N}\\s]", ""); // cho phép chữ cái & số Unicode

        // Viết hoa chữ cái đầu của mỗi từ
        Pattern pattern = Pattern.compile("\\b\\p{L}");
        Matcher matcher = pattern.matcher(cleanName.toLowerCase());

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group().toUpperCase());
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public String cleanDescription(String description) {
        if (description == null || description.isEmpty()) {
            return description;
        }

        String result = description.trim()
                .replaceAll("\\s+", " ")          // gom khoảng trắng thừa
                .replaceAll("\\s([.,;?!])", "$1") // bỏ khoảng trắng trước dấu câu
                .replaceAll("([,])([^\\s])", "$1 $2"); // thêm space sau dấu phẩy nếu thiếu

        // Viết hoa chữ cái đầu
        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }
}
