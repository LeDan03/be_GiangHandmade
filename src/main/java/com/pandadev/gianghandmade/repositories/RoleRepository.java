package com.pandadev.gianghandmade.repositories;

import com.pandadev.gianghandmade.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(String name);

    boolean existsByIdNotNull();

    Optional<Role> findByName(String name);
}
