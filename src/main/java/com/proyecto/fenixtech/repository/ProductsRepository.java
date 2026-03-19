package com.proyecto.fenixtech.repository;

import com.proyecto.fenixtech.model.Products;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductsRepository extends JpaRepository<Products, Integer> {

        @Query(value = "SELECT * FROM products WHERE product_id = :productId AND status = 'ACTIVE'", nativeQuery = true)
        Optional<Products> findByProductIdAndProductStatusActive(@Param("productId") Integer productId);

        @Query(value = "SELECT * FROM products WHERE status = 'ACTIVE' AND title LIKE CONCAT('%', :title, '%')", nativeQuery = true)
        List<Products> findByProductStatusActiveAndProductTitleContainingIgnoreCase(@Param("title") String title);

        @Query(value = "SELECT * FROM products WHERE status = 'ACTIVE' AND subcategory_id = :subcategoryId", nativeQuery = true)
        List<Products> findByProductStatusActiveAndSubcategory_SubcategoryId(
                        @Param("subcategoryId") Integer subcategoryId);

        @Query(value = "SELECT * FROM products WHERE status = 'ACTIVE' AND company_id = :companyId", nativeQuery = true)
        List<Products> findByProductStatusActiveAndCompany_CompanyId(@Param("companyId") Integer companyId);

        @Query(value = "SELECT * FROM products WHERE company_id = :companyId", nativeQuery = true)
        List<Products> findByCompany_CompanyId(@Param("companyId") Integer companyId);

        @Modifying
        @Query("UPDATE Products p SET p.productStatus = 'HIDDEN' WHERE p.company.companyId = :companyId")
        void hideAllByCompanyId(@Param("companyId") Integer companyId);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM cart_items WHERE product_id = :productId", nativeQuery = true)
        void deleteCartItemsByProductId(@Param("productId") Integer productId);

        @Modifying
        @Transactional
        @Query(value = "DELETE ci FROM cart_items ci " +
                        "INNER JOIN products p ON ci.product_id = p.product_id " +
                        "WHERE p.company_id = :companyId", nativeQuery = true)
        void deleteCartItemsByCompanyId(@Param("companyId") Integer companyId);

        @Query(value = "SELECT p.* FROM products p " +
                        "INNER JOIN companies c ON p.company_id = c.company_id " +
                        "INNER JOIN users u ON c.user_id = u.user_id " +
                        "WHERE u.is_active = TRUE " +
                        "AND p.status = 'ACTIVE' " +
                        "AND (:lType IS NULL OR p.listing_type = :lType) " +
                        "AND (:cStatus IS NULL OR p.condition_status = :cStatus) " +
                        "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
                        "AND (:minStock IS NULL OR p.stock_quantity >= :minStock) " +
                        "AND (:maxStock IS NULL OR p.stock_quantity <= :maxStock)" +
                        "AND (:location IS NULL OR p.location LIKE CONCAT('%', :location, '%'))", nativeQuery = true)
        List<Products> findByConditions(
                        @Param("lType") String lType,
                        @Param("cStatus") String cStatus,
                        @Param("minPrice") Double minPrice,
                        @Param("maxPrice") Double maxPrice,
                        @Param("minStock") Integer minStock,
                        @Param("maxStock") Integer maxStock,
                        @Param("location") String location);

}
