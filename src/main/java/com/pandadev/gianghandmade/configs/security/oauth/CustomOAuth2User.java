package com.pandadev.gianghandmade.configs.security.oauth;

import com.pandadev.gianghandmade.entities.Image;
import com.pandadev.gianghandmade.entities.User;
import com.pandadev.gianghandmade.responses.ImageResponse;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Data
public class CustomOAuth2User implements OAuth2User {
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private int id;
    private String name;
    private String email;
    private String gender;
    private Image avatar;
    private String role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            User user) {
        this.authorities = authorities;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();

        this.gender = user.getGender() != null ? user.getGender().name() : "MALE";

        this.role = (user.getRole() != null && user.getRole().getName() != null)
                ? user.getRole().getName()
                : "USER"; // fallback mặc định
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return attributes.get(nameAttributeKey).toString();
    }

}
