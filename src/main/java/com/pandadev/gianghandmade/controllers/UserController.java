package com.pandadev.gianghandmade.controllers;

import com.pandadev.gianghandmade.responses.UserResponse;
import com.pandadev.gianghandmade.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/me")
    public ResponseEntity<UserResponse> findByAccessToken(@CookieValue(value = "accessToken") String accessToken) {
        return  ResponseEntity.ok().body(userService.findByAccessToken(accessToken));
    }

}
