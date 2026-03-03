package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.CartItems;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer>{
    List<CartItems> findByUser_UserId(Integer id);
    List<CartItems> findByProduct_ProductId(Integer id);
    
    @Query(value = "SELECT * FROM cart_items WHERE " +
           "(:minQty IS NULL OR quantity >= :minQty) AND " +
           "(:maxQty IS NULL OR quantity <= :maxQty)", 
           nativeQuery = true)
    List<CartItems> findByQuantityFilters(
            @Param("minQty") Integer minQty, 
            @Param("maxQty") Integer maxQty
    );


    


}

    
