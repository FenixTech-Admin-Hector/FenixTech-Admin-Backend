package com.proyecto.fenixtech.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.Orders;

import java.time.LocalDateTime;
import java.util.List;


public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByBuyer_UserId(Integer id);
    @Query(value = "SELECT * FROM orders WHERE " +
           "(:minAmount IS NULL OR total_amount >= :minAmount) AND " +
           "(:maxAmount IS NULL OR total_amount <= :maxAmount) AND " +
           "(:minDate IS NULL OR order_date >= :minDate) AND " +
           "(:maxDate IS NULL OR order_date <= :maxDate) AND " +
           "(:status IS NULL OR status = :status) AND " +
           "(:requiresShipping IS NULL OR requires_shipping = :requiresShipping)", 
           nativeQuery = true
          )
    List<Orders> findByConditions(
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            @Param("minDate") LocalDateTime minDate,
            @Param("maxDate") LocalDateTime maxDate,
            @Param("status") String status,
            @Param("requiresShipping") Boolean requiresShipping
    );




}

