package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.entities.User;
import com.pandadev.gianghandmade.exceptions.ForbiddenException;
import com.pandadev.gianghandmade.exceptions.NotFoundException;
import com.pandadev.gianghandmade.mappers.UserMapper;
import com.pandadev.gianghandmade.repositories.UserRepository;
import com.pandadev.gianghandmade.responses.UserResponse;
import com.pandadev.gianghandmade.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper  userMapper;
    private final JWTUtil jwtUtil;

    public UserResponse findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new NotFoundException("Không tìm thấy tài khoản với email: "+ email);
        }
        return userMapper.toUserResponse(user.get());
    }

    public UserResponse findByAccessToken(String accessToken) {
        Long userId = jwtUtil.extractUserIdFromAccess(accessToken);
        if(userId==null){
            throw new ForbiddenException("Yêu cầu không bị từ chối, token không hợp lệ");
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Không tìm thấy người dùng");
        }
        return userMapper.toUserResponse(user.get());
    }
}
