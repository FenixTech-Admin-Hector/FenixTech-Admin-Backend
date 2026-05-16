package com.proyecto.fenixtech.admin.order;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.fenixtech.model.Orders;
import com.proyecto.fenixtech.model.OrderDetails;
import com.proyecto.fenixtech.model.enums.OrderStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Orders", description = "API para la gestión y control de ventas por parte del Administrador")
@RequestMapping("/api/admin/orders")
@RestController
public class AdminOrdersController {

    @Autowired
    private AdminOrdersService adminOrdersService;

    @Operation(summary = "Listar y filtrar todos los pedidos", description = "Muestra el historial completo de ventas con filtros de fecha, estado y montos.")
    @GetMapping
    public ResponseEntity<List<Orders>> findAllAdmin(
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxDate,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Boolean requiresShipping) {
        
        return ResponseEntity.ok(adminOrdersService.findOrdersAdmin(minAmount, maxAmount, minDate, maxDate, status, requiresShipping));
    }

    @Operation(summary = "Ver detalles de un pedido", description = "Devuelve la información detallada (artículos) de un pedido concreto.")
    @GetMapping("/{id}")
    public ResponseEntity<List<OrderDetails>> getOrderDetails(@PathVariable Integer id) {
        return ResponseEntity.ok(adminOrdersService.findOrderDetails(id));
    }

    @Operation(summary = "Cancelar pedido", description = "Cambia el estado a CANCELLED y restaura automáticamente el stock de los productos.")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Integer id) {
        adminOrdersService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}