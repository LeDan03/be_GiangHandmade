package com.pandadev.gianghandmade.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEmailRoleDto {
    private String email;
    private String role;

    public UserEmailRoleDto(String email, String role) {
        this.email = email;
        this.role = role;
    }
}
