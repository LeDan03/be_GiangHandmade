package com.pandadev.gianghandmade.repositories;

import com.pandadev.gianghandmade.entities.Category;
import com.pandadev.gianghandmade.entities.Product;
import com.pandadev.gianghandmade.entities.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    boolean existsByCategoryId(int categoryId);
//    void changeStatusById(ProductStatus status,int id);

    List<Product> findByCategoryId(int categoryId);
    List<Product> findByStatus(ProductStatus status);

    List<Product> findAllByOrderByCreatedAtDesc();//Moi nhat
    List<Product> findAllByOrderByCreatedAt();//Cu nhat

    @Modifying
    @Query(value = """
            UPDATE Product p
            SET p.category=(SELECT c FROM Category c WHERE c.id=:categoryId)
            WHERE p.id IN :productIds
            """)
    void changeCategoryByIdIn(@Param("categoryId") int categoryId, @Param("productIds") List<Integer> productIds);

    @Query(value = """
        SELECT * FROM product p
        WHERE p.name COLLATE utf8mb4_general_ci LIKE CONCAT('%', :keyword, '%')
           OR p.description COLLATE utf8mb4_general_ci LIKE CONCAT('%', :keyword, '%')
        ORDER BY p.created_at DESC
        """, nativeQuery = true)
    List<Product> search(@Param("keyword") String keyword);
}
