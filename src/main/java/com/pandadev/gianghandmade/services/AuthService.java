package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.entities.User;
import com.pandadev.gianghandmade.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public String login(String email, String password){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(token);

        User user = (User) authentication.getPrincipal();
        return user.getUsername();//tạo token
    }
}
