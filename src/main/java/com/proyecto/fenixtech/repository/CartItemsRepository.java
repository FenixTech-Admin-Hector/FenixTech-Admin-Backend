package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.CartItems;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer>{
    List<CartItems> findByUser_UserId(Integer id);
    List<CartItems> findByProduct_ProductId(Integer id);
    List<CartItems> findByQuantityGreaterThan(Integer quantity);
    List<CartItems> findByQuantityLessThan(Integer quantity);


}

    
