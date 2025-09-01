package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.entities.Role;
import com.pandadev.gianghandmade.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init(){
        Role user = new Role();
        user.setName("USER");
        Role admin = new Role();
        admin.setName("ADMIN");

        if(!roleRepository.existsByIdNotNull()){
            roleRepository.saveAll(List.of(user,admin));
        }
    }
}
