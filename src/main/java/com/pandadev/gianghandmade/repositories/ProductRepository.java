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

    List<Product> findByCategoryId(int categoryId);
    List<Product> findByStatus(ProductStatus status);

    List<Product> findAllByOrderByCreatedAtDesc();//Moi nhat
    List<Product> findAllByOrderByCreatedAt();//Cu nhat

    @Modifying
    @Query(value = """
            UPDATE Product p
            SET p.category=(SELECT c FROM Category c WHERE c.id=:categoryId)
            WHERE p.id=:productId
            """)
    void changeCategoryById(@Param("categoryId") int categoryId, @Param("productId") int productId);

    @Modifying
    @Query(value = """
            UPDATE Product p
            SET p.category=(SELECT c FROM Category c WHERE c.id=:categoryId)
            WHERE p.id IN :productIds
            """)
    void changeCategoryByIdIn(@Param("categoryId") int categoryId, @Param("productIds") List<Integer> productIds);

    @Query(value = """
        SELECT * FROM product
        WHERE MATCH(name, description) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
        ORDER BY MATCH(name, description) AGAINST(:keyword) DESC
        """,
            nativeQuery = true)
    List<Product> search(@Param("keyword") String keyword);
}
