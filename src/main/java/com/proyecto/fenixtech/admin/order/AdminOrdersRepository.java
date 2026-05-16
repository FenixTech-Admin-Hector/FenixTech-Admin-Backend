package com.proyecto.fenixtech.admin.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.fenixtech.model.Orders;

public interface AdminOrdersRepository extends JpaRepository<Orders, Integer> {
    
    @Query(value = "SELECT * FROM orders WHERE " +
           "(:minAmount IS NULL OR total_amount >= :minAmount) AND " +
           "(:maxAmount IS NULL OR total_amount <= :maxAmount) AND " +
           "(:minDate IS NULL OR order_date >= :minDate) AND " +
           "(:maxDate IS NULL OR order_date <= :maxDate) AND " +
           "(:status IS NULL OR status = :status) AND " +
           "(:requiresShipping IS NULL OR requires_shipping = :requiresShipping)", 
           nativeQuery = true)
    List<Orders> findByConditions(
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            @Param("minDate") LocalDateTime minDate,
            @Param("maxDate") LocalDateTime maxDate,
            @Param("status") String status,
            @Param("requiresShipping") Boolean requiresShipping
    );

    // 🚀 MÉTODO AÑADIDO PARA EL DASHBOARD (Suma total de ingresos)
    @Query(value = "SELECT SUM(total_amount) FROM orders WHERE status = :status", nativeQuery = true)
    Double sumTotalAmountByStatus(@Param("status") String status);
}