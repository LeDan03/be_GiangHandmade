package com.pandadev.gianghandmade.mappers;

import com.pandadev.gianghandmade.entities.User;
import com.pandadev.gianghandmade.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserResponse toUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse toUserResponse(OAuth2User oAuth2User) {
        return modelMapper.map(oAuth2User, UserResponse.class);
    }

    public List<UserResponse> toUserResponses(List<User> users) {
        return users.stream().map(this::toUserResponse).collect(Collectors.toList());
    }
}
