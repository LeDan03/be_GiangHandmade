package com.pandadev.gianghandmade.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
    private UserResponse user;
    private String accessToken;
    private String refreshToken;
}
