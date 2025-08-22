package com.pandadev.gianghandmade.configs.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPrincipal {
    private long userId;
    private String email;
}
