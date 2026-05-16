package com.proyecto.fenixtech.admin.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.enums.ProductStatus;

public interface AdminProductsRepository extends JpaRepository<Products, Integer> {

    // 🚀 BÚSQUEDA AVANZADA PARA LA TABLA DEL ADMINISTRADOR
    @Query(value = "SELECT * FROM products p " +
            "WHERE (:status IS NULL OR p.status = :status) " +
            "AND (:subcategoryId IS NULL OR p.subcategory_id = :subcategoryId) " +
            "AND (:companyId IS NULL OR p.company_id = :companyId) " +
            "AND (:title IS NULL OR p.title LIKE CONCAT('%', :title, '%'))", nativeQuery = true)
    List<Products> findProductsAdminFilters(
            @Param("status") String status,
            @Param("subcategoryId") Integer subcategoryId,
            @Param("companyId") Integer companyId,
            @Param("title") String title);

    @Modifying
    @Query(value = "DELETE FROM cart_items WHERE product_id = :productId", nativeQuery = true)
    void deleteCartItemsByProductId(@Param("productId") Integer productId);

    // 🚀 MÉTODO AÑADIDO PARA EL DASHBOARD (Conteo de productos por estado)
    long countByProductStatus(ProductStatus status);
}