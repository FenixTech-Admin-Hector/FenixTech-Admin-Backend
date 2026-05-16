package com.proyecto.fenixtech.admin.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.OrderDetails;

public interface AdminOrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    List<OrderDetails> findByOrder_OrderId(Integer id);
}