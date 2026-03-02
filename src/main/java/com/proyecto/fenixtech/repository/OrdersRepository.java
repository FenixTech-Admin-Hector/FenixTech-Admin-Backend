package com.proyecto.fenixtech.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.Orders;
import com.proyecto.fenixtech.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;


public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByBuyer_UserId(Integer id);
    List<Orders> findByStatus(OrderStatus status);
    List<Orders> findByTotalAmountGreaterThan(Double amount);
    List<Orders> findByTotalAmountLessThan(Double amount);
    List<Orders> findByRequiresShipping(Boolean requiresShipping);
    List<Orders> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    List<Orders> findByStatusAndRequiresShipping(Boolean requiresShipping, OrderStatus status);


}

