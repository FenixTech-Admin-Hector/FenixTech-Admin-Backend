package com.proyecto.fenixtech.admin.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.OrderDetails;
import com.proyecto.fenixtech.model.Orders;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.enums.OrderStatus;
import com.proyecto.fenixtech.model.enums.ProductStatus;
import com.proyecto.fenixtech.repository.ProductsRepository; // Repositorio del grupo para restaurar stock

@Service
public class AdminOrdersService {

    @Autowired
    private AdminOrdersRepository adminOrdersRepository;

    @Autowired
    private AdminOrderDetailsRepository adminOrderDetailsRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Transactional(readOnly = true)
    public List<Orders> findOrdersAdmin(Double minAmount, Double maxAmount, LocalDate minDate, LocalDate maxDate, OrderStatus status, Boolean requiresShipping) {
        LocalDateTime startDT = (minDate != null) ? minDate.atStartOfDay() : null;
        LocalDateTime endDT = (maxDate != null) ? maxDate.atTime(23, 59, 59) : null;
        String statusStr = (status != null) ? status.name() : null;

        return adminOrdersRepository.findByConditions(minAmount, maxAmount, startDT, endDT, statusStr, requiresShipping);
    }

    @Transactional(readOnly = true)
    public List<OrderDetails> findOrderDetails(Integer orderId) {
        // Aseguramos que el pedido existe antes de buscar sus detalles
        adminOrdersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + orderId));
        return adminOrderDetailsRepository.findByOrder_OrderId(orderId);
    }

    @Transactional
    public void cancelOrder(Integer id) {
        Orders order = adminOrdersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Este pedido ya se encuentra cancelado.");
        }

        // Lógica de restauración de stock iterando sobre los OrderDetails
        for (OrderDetails detail : order.getOrderDetails()) {
            Products product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());

            // Si estaba agotado, lo volvemos a poner activo al devolver el stock
            if (product.getProductStatus() == ProductStatus.SOLD_OUT) {
                product.setProductStatus(ProductStatus.ACTIVE);
            }
            productsRepository.save(product);
        }

        // Marcamos el pedido como cancelado
        order.setStatus(OrderStatus.CANCELLED);
        adminOrdersRepository.save(order);
    }
}