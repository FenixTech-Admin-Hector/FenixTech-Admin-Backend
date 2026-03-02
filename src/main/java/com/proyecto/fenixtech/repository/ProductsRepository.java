package com.proyecto.fenixtech.repository;
import com.proyecto.fenixtech.model.Products;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.enums.ConditionStatus;
import com.proyecto.fenixtech.model.enums.ListingType;
import com.proyecto.fenixtech.model.enums.ProductStatus;




public interface ProductsRepository extends JpaRepository<Products, Integer>{
    List<Products> findByProductStatus(ProductStatus productStatus);
    List<Products> findByListingType(ListingType listingType);
    List<Products> findByStatus(ConditionStatus status);
    List<Products> findByCategory_CategoryId(Integer id);
    List<Products> findByCompany_CompanyId(Integer id);
    List<Products> findByPriceGreaterThan(Double price);
    List<Products> findByPriceLessThan(Double price);
    List<Products> findByStockGreaterThan(Integer stock);
    List<Products> findByStockLessThan(Integer stock);
    List<Products> findByProductTitleContainingIgnoreCase(String title);
    List<Products> findByStockEquals(Integer stock);

    @Query(value = "SELECT *.p FROM products p WHERE " +
           "(:pStatus IS NULL OR p.productStatus = :pStatus) AND " +
           "(:lType IS NULL OR p.listingType = :lType) AND " +
           "(:cStatus IS NULL OR p.status = :cStatus)", nativeQuery = true)
    List<Products> findByMultipleFilters(
            @Param("pStatus") ProductStatus pStatus, 
            @Param("lType") ListingType lType, 
            @Param("cStatus") ConditionStatus cStatus
    );
    
    

} 
