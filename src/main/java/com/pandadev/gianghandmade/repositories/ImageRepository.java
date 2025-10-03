package com.pandadev.gianghandmade.repositories;

import com.pandadev.gianghandmade.entities.Image;
import com.pandadev.gianghandmade.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    List<ProductImage> findByProductId(Integer productId);
    boolean existsByIdNotNull();
    boolean existsById(Long id);

    Optional<Image> findByPublicId(String publicId);
}
