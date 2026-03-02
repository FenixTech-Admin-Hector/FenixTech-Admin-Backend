package com.proyecto.fenixtech.repository;
import com.proyecto.fenixtech.model.Products;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductsRepository extends JpaRepository<Products, Integer>{
    List<Products> findByCategory_CategoryId(Integer id);
    List<Products> findByCompany_CompanyId(Integer id);
    List<Products> findByProductTitleContainingIgnoreCase(String title);

    @Query(value = "SELECT * FROM products WHERE " +
           "(:pStatus IS NULL OR status = :pStatus) AND " +
           "(:lType IS NULL OR listing_type = :lType) AND " +
           "(:cStatus IS NULL OR condition_status = :cStatus) AND " +
           "(:minPrice IS NULL OR price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR price <= :maxPrice) AND " +
           "(:minStock IS NULL OR stock_quantity >= :minStock) AND " +
           "(:maxStock IS NULL OR stock_quantity <= :maxStock)", 
           nativeQuery = true)
    List<Products> findByConditions(
            @Param("pStatus") String pStatus, 
            @Param("lType") String lType, 
            @Param("cStatus") String cStatus,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minStock") Integer minStock,
            @Param("maxStock") Integer maxStock
    );
    
    

} 
