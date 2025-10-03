package com.pandadev.gianghandmade.mappers;

import com.pandadev.gianghandmade.entities.Role;
import com.pandadev.gianghandmade.entities.User;
import com.pandadev.gianghandmade.entities.enums.AuthProviders;
import com.pandadev.gianghandmade.entities.enums.Gender;
import com.pandadev.gianghandmade.exceptions.NotFoundException;
import com.pandadev.gianghandmade.repositories.RoleRepository;
import com.pandadev.gianghandmade.repositories.UserRepository;
import com.pandadev.gianghandmade.requests.RegisterRequest;
import com.pandadev.gianghandmade.responses.UserResponse;
import com.pandadev.gianghandmade.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    @Value("{male.user.default.avt}")
    private String maleAvatarUrl;

    @Value("{female.user.default.avt}")
    private String femaleAvatarUrl;

    @Value("{other.user.default.avt}")
    private String otherAvatarUrl;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;
    private final ImageMapper imageMapper;

    private final ImageUtil imageUtil;
    private final UserRepository userRepository;

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getUsername())
                .gender(user.getGender().name())
                .avatar(imageMapper.toAvatarResponse(user.getAvatar()))
                .role(user.getRole().getName())
                .cartId(user.getCart().getId())
                .build();
    }

    public UserResponse toUserResponse(OAuth2User oAuth2User) {
        return modelMapper.map(oAuth2User, UserResponse.class);
    }

    public User toUser(RegisterRequest registerRequest) {
        Optional<Role> userOpt = roleRepository.findByName("USER");
        if(userOpt.isEmpty()){
            throw new NotFoundException("Role USER không tồn tại");
        }
        Role user = userOpt.get();
        return User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(user)
                .name(registerRequest.getName())
                .gender(Gender.valueOf(registerRequest.getGender().toUpperCase()))
                .authProviders(AuthProviders.valueOf(registerRequest.getAuthProviders().toUpperCase()))
                .avatar(imageUtil.getAvatarByGender(registerRequest.getGender().toUpperCase()))
                .build();
    }

    public List<UserResponse> toUserResponses(List<User> users) {
        return users.stream().map(this::toUserResponse).collect(Collectors.toList());
    }
}
