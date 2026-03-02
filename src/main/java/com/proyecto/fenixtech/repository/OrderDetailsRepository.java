package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.OrderDetails;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {

    List<OrderDetails> findByOrder_OrderId(Integer id);
    List<OrderDetails> findByProduct_ProductId(Integer id);
    @Query(value = "SELECT * FROM order_details WHERE " +
           "(:minPrice IS NULL OR unit_price_at_purchase >= :minPrice) AND " +
           "(:maxPrice IS NULL OR unit_price_at_purchase <= :maxPrice) AND " +
           "(:minQty IS NULL OR quantity >= :minQty) AND " +
           "(:maxQty IS NULL OR quantity <= :maxQty)", 
           nativeQuery = true)
    List<OrderDetails> findByPriceAndQuantityFilters(
            @Param("minPrice") Double minPrice, 
            @Param("maxPrice") Double maxPrice, 
            @Param("minQty") Integer minQty, 
            @Param("maxQty") Integer maxQty
    );
    
} 