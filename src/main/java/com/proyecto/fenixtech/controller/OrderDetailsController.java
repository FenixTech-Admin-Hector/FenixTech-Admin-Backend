package com.proyecto.fenixtech.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.fenixtech.service.OrderDetailsService;
import com.proyecto.fenixtech.model.OrderDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "OrderDetails", description = "API para gestión de detalles de pedidos")
@RequestMapping("/order_details")
@RestController
public class OrderDetailsController {
    @Autowired
    private OrderDetailsService orderDetailsService;

    @Operation(summary = "Obtener todos los detalles de pedido", description = "Devuelve una lista de todos los detalles de pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles obtenidos con éxito")
    })
    @GetMapping
    public ResponseEntity<List<OrderDetails>> findAllOrderDetails() {
        return ResponseEntity.status(HttpStatus.OK).body(orderDetailsService.findAllOrderDetails());
    }

    @Operation(summary = "Obtener detalle de pedido por ID", description = "Devuelve un detalle de pedido por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetails> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderDetailsService.findById(id));
    }

    @Operation(summary = "Obtener detalles por ID de pedido", description = "Devuelve una lista de detalles asociados a un ID de pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/order/{id}")
    public ResponseEntity<List<OrderDetails>> findByOrderId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderDetailsService.findByOrderId(id));
    }

    @Operation(summary = "Obtener detalles por ID de producto", description = "Devuelve una lista de detalles asociados a un ID de producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/product/{id}")
    public ResponseEntity<List<OrderDetails>> findByProductId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderDetailsService.findByProductId(id));
    }

    @Operation(summary = "Obtener el numero de detalles de pedido", description = "Devuelve el numero de detalles de pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Numero de detalles de pedido obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = orderDetailsService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Obtener detalles de pedido por precio y cantidad", description = "Devuelve una lista de detalles de pedido filtrados por precio y cantidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles de pedido obtenidos con éxito")
    })
    @GetMapping("/filters")
    public ResponseEntity<List<OrderDetails>> findByPriceAndQuantityFilters(
            @RequestParam(required = false, defaultValue = "1.00") Double minPrice, @RequestParam(required = false) Double maxPrice, 
            @RequestParam(required = false, defaultValue = "1")Integer minQty, @RequestParam(required = false)
            Integer maxQty) {
        return ResponseEntity.status(HttpStatus.OK).body(orderDetailsService.findByPriceAndQuantityFilters(minPrice, maxPrice, minQty, maxQty));
    }
}
