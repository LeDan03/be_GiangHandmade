package com.pandadev.gianghandmade.entities;

import com.pandadev.gianghandmade.entities.enums.AuthProviders;
import com.pandadev.gianghandmade.entities.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column()
    private String email;

    @Column()
    private String username;

    @Column()
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private AuthProviders authProviders;

    @Column
    private String providerId;

    @Column
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
