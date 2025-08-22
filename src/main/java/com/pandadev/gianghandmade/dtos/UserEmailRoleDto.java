package com.pandadev.gianghandmade.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailRoleDto {
    private String email;
    private String role;
}
