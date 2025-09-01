package com.pandadev.gianghandmade.repositories;

import com.pandadev.gianghandmade.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByIdNotNull();
    boolean existsByName(String name);
}
