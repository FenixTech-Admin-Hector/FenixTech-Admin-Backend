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

import com.proyecto.fenixtech.model.CartItems;
import com.proyecto.fenixtech.service.CartItemsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "CartItems", description = "API para gestión de items del carrito")
@RequestMapping("/api/cart_items")
@RestController
public class CartItemsController {
    @Autowired
    private CartItemsService cartItemsService;

    @Operation(summary = "Obtener todos los items del carrito", description = "Devuelve una lista de todos los items del carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items obtenidos con éxito")
    })
    @GetMapping
    public ResponseEntity<List<CartItems>> findAllCartItems() {
        return ResponseEntity.status(HttpStatus.OK).body(cartItemsService.findAllCartItems());
    }

    @Operation(summary = "Obtener item del carrito por ID", description = "Devuelve un item del carrito por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CartItems> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(cartItemsService.findById(id));
    }

    @Operation(summary = "Obtener items del carrito por ID de usuario", description = "Devuelve una lista de items asociados a un ID de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<CartItems>> findByUserId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(cartItemsService.findByUserId(id));
    }

    @Operation(summary = "Obtener items del carrito por ID de producto", description = "Devuelve una lista de items asociados a un ID de producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/product/{id}")
    public ResponseEntity<List<CartItems>> findByProductId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(cartItemsService.findByProductId(id));
    }

    @Operation(summary = "Obtener items del carrito en función de la cantidad", description = "Devuelve una lista de items con en función de un rango de catidades")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Items obtenidos con éxito")
    })
    @GetMapping("/quantity")
    public ResponseEntity<List<CartItems>> findByQuantityFilters(
            @RequestParam(required = false, defaultValue = "1") Integer minQty,
            @RequestParam(required = false) Integer maxQty){
    return ResponseEntity.status(HttpStatus.OK).body(cartItemsService.findByQuantityFilters(minQty, maxQty));
            }

    @Operation(summary = "Obtener el numero de items del carrito", description = "Devuelve el numero de items del carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Numero de items del carrito obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = cartItemsService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
