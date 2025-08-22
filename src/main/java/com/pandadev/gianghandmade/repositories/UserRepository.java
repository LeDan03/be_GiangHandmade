package com.pandadev.gianghandmade.repositories;

import com.pandadev.gianghandmade.dtos.UserEmailRoleDto;
import com.pandadev.gianghandmade.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("""
            SELECT new com.pandadev.gianghandmade.dtos.UserEmailRoleDto(u.email, u.role)
            FROM User u
            WHERE u.id=:id
            """)
    Optional<UserEmailRoleDto> findUserEmailRoleById(@Param("id") long id);
}
