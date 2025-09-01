package com.pandadev.gianghandmade.utils;

import com.pandadev.gianghandmade.exceptions.BadRequestException;
import com.pandadev.gianghandmade.requests.RegisterRequest;
import com.pandadev.gianghandmade.services.BannedWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidationUtil {

    private final BannedWordService bannedWordService;

    private static final int MIN_PASSWORD_LENGTH = 8;

    public void validateRegistration(RegisterRequest request) {
        validateUsername(request.getName());
        validatePassword(request.getPassword());
    }

    private void validateUsername(String username) {
        if (bannedWordService.containsWord(username)) {
            throw new BadRequestException("Tên người dùng chứa từ ngữ không phù hợp");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new BadRequestException("Tên người dùng phải dài từ 3 đến 50 kí tự");
        }
    }

    private void validatePassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new BadRequestException("Mật khẩu phải dài tối thiểu " + MIN_PASSWORD_LENGTH + " kí tự");
        }
    }
}

