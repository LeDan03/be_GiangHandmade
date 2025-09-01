package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.configs.security.oauth.CustomOAuth2User;
import com.pandadev.gianghandmade.entities.User;
import com.pandadev.gianghandmade.entities.enums.AuthProviders;
import com.pandadev.gianghandmade.entities.enums.Gender;
import com.pandadev.gianghandmade.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String email = oAuth2User.getAttributes().get("email").toString();
        String name = oAuth2User.getAttributes().get("name").toString();
        String picture = oAuth2User.getAttributes().get("picture").toString();
        String providerId = oAuth2User.getAttributes().get("sub").toString();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setPassword(passwordEncoder.encode("rdPassword-" + UUID.randomUUID()));
                    newUser.setName(name);
                    newUser.setAvatarUrl(picture);
                    newUser.setGender(Gender.MALE);
                    newUser.setAuthProviders(AuthProviders.GOOGLE);
                    newUser.setProviderId(providerId);
                    userRepository.save(newUser);
                    return newUser;
                });
        if (user.getProviderId() == null) {
            user.setProviderId(providerId);
            userRepository.save(user);
        }
        return new CustomOAuth2User(
                oAuth2User.getAuthorities(),
                oAuth2User.getAttributes(),
                "email",
                user
        );
    }

}
