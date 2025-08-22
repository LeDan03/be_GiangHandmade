package com.pandadev.gianghandmade.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
    private long id;
    private String username;
    private String email;
    private String gender;
    private String avatarUrl;
    private String role;
}
