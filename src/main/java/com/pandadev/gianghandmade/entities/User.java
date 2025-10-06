package com.pandadev.gianghandmade.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pandadev.gianghandmade.entities.enums.AuthProviders;
import com.pandadev.gianghandmade.entities.enums.Gender;
import com.pandadev.gianghandmade.entities.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column()
    private String email;

    @Column(unique = true)
    private String name;

    @Column()
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private AuthProviders authProviders;

    @Column
    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatarId",referencedColumnName = "id")
    private Avatar avatar;

    @ManyToOne
    @JoinColumn(name = "roleId", referencedColumnName = "id")
    @JsonBackReference
    private Role role;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference
    private List<Order> orders;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference
    private List<ShippingInfo> shippingInfos;

    @Override
    public boolean isEnabled() {
        return this.status.equals(UserStatus.ACTIVE);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

}
