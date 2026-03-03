package com.proyecto.fenixtech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.fenixtech.service.OrdersService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.proyecto.fenixtech.model.Orders;
import com.proyecto.fenixtech.model.enums.OrderStatus;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Orders", description = "API para gestión de pedidos")
@RequestMapping("/api/orders")
@RestController
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @Operation(summary = "Obtener todos los pedidos", description = "Devuelve una lista de todos los pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos obtenidos con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Orders>> findAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findAllOrders());
    }

    @Operation(summary = "Obtener pedido por ID", description = "Devuelve un pedido por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Orders> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findById(id));
    }

    @Operation(summary = "Obtener pedidos por ID de comprador", description = "Devuelve una lista de pedidos asociados a un ID de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/buyer/{id}")
    public ResponseEntity<List<Orders>> findByBuyerId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findByBuyerId(id));
    }

    @Operation(summary = "Obtener pedidos por estado", description = "Devuelve una lista de pedidos filtrados por su estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos obtenidos con éxito")
    })
    @GetMapping("/status")
    public ResponseEntity<List<Orders>> findByStatus(@RequestParam OrderStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findByStatus(status));
    }

    @Operation(summary = "Obtener pedidos con importe mayor que el introducido", description = "Devuelve una lista de pedidos con importe mayor que el introducido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos obtenidos con éxito")
    })
    @GetMapping("/total_amount/greater")
    public ResponseEntity<List<Orders>> findByTotalAmountGreaterThen(@RequestParam (required = true) Double amount){
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findByTotalAmountGreaterThan(amount));
    }

    
    @Operation(summary = "Obtener pedidos con importe menor que el introducido", description = "Devuelve una lista de pedidos con importe menor que el introducido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos obtenidos con éxito")
    })
    @GetMapping("/total_amount/less")
    public ResponseEntity<List<Orders>> findByTotalAmountLessThen(@RequestParam (required = true) Double amount){
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findByTotalAmountLessThan(amount));
    }

    @Operation(summary = "Obtener pedidos con envio", description = "Devuelve una lista de pedidos con envio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos obtenidos con éxito")
    })
    @GetMapping("/shipping")
    public ResponseEntity<List<Orders>> findByShipping(@RequestParam(required = true) Boolean requiresShipping){
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findByShipping(requiresShipping));
    }

    @Operation(summary = "Obtener pedidos por estado y envio", description = "Devuelve una lista de pedidos con estado y envio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos obtenidos con éxito")
    })
    @GetMapping("/conditions")
    public ResponseEntity<List<Orders>> findByStatusAndShipping(@RequestParam(required = true) OrderStatus orderStatus,@RequestParam(required = true)Boolean requiresShipping){
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findByStatusAndRequiresShipping(requiresShipping, orderStatus));
    }

    @Operation(summary = "Obtener pedidos por año", description = "Devuelve una lista de pedidos por un año en concreto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos obtenidos con éxito")
    })
    @GetMapping("/year")
    public ResponseEntity<List<Orders>> findByYear(@RequestParam(required = true) Integer year){
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findByOrderDate(year));
    }

    @Operation(summary = "Obtener pedidos  un rango de fechas", description = "Devuelve una lista de pedidos por un rango de fechas en concreto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos obtenidos con éxito")
    })
    @GetMapping("/date")
    public ResponseEntity<List<Orders>> findByDate(@RequestParam(required = true) LocalDate dateStart, @RequestParam(required = true) LocalDate dateEnd){
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findByOrderDateBetween(dateStart, dateEnd));
    }

    @Operation(summary = "Obtener el numero de pedidos", description = "Devuelve el numero de pedidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Numero de pedidos obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = ordersService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }






}
