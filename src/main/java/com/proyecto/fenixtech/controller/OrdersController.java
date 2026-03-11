package com.proyecto.fenixtech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.fenixtech.service.OrdersService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.proyecto.fenixtech.dto.OrderRequestUpdateDTO;
import com.proyecto.fenixtech.dto.OrdersRequestDTO;
import com.proyecto.fenixtech.model.Orders;
import com.proyecto.fenixtech.model.enums.OrderStatus;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Orders", description = "API para gestión de pedidos")
@RequestMapping("/orders")
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

        @Operation(summary = "Obtener pedidos por una serie de filtros", description = "Devuelve una lista de pedidos filtrados por su estado, tipo de venta, importe y fecha")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Pedidos obtenidos con éxito")
        })
        @GetMapping("/filters")
        public ResponseEntity<List<Orders>> findByConditions(
                        @RequestParam(required = false) Double minAmount,
                        @RequestParam(required = false) Double maxAmount,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minDate,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxDate,
                        @RequestParam(required = false) OrderStatus status,
                        @RequestParam(required = false) Boolean requiresShipping) {

                return ResponseEntity.status(HttpStatus.OK)
                                .body(ordersService.findByConditions(minAmount, maxAmount, minDate, maxDate, status,
                                                requiresShipping));
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

        @Operation(summary = "Crear un pedido a partir del carrito del usuario", description = "Crea un pedido a partir del carrito del usuario")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Pedido creado con éxito"),
                        @ApiResponse(responseCode = "404", description = "No se puede crear el pedido")
        })
        @PostMapping("/checkout")
        public ResponseEntity<Orders> create(@Valid @RequestBody OrdersRequestDTO dto) {
                return ResponseEntity.status(HttpStatus.CREATED).body(ordersService.createOrderFromUserCart(dto));
        }

        @Operation(summary = "Eliminar un pedido por su ID", description = "Elimina un pedido por su ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Pedido eliminado con éxito"),
                        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
                ordersService.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        @Operation(summary = "Actualizar estado del pedido", description = "Cambia el estado logístico y gestiona el stock en caso de cancelación.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estado actualizado con éxito"),
                        @ApiResponse(responseCode = "400", description = "Cambio de estado no permitido"),
                        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
        })
        @PutMapping("/{id}/status")
        public ResponseEntity<Orders> updateStatus(
                        @PathVariable Integer id,
                        @Valid @RequestBody OrderRequestUpdateDTO dto) {

                return ResponseEntity.ok(ordersService.updateStatus(id, dto));
        }

}
