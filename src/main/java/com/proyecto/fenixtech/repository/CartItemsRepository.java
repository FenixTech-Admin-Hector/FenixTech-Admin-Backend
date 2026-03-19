package com.proyecto.fenixtech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.CartItems;

import jakarta.transaction.Transactional;

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

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_items WHERE user_id = :userId", nativeQuery = true)
    void deleteByUser_UserId(@Param("userId") Integer userId);

    Optional<CartItems> findByUser_UserIdAndProduct_ProductId(Integer userId, Integer productId);
    
    Long countByUser_UserId(Integer id);

}

    
