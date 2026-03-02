package com.proyecto.fenixtech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.fenixtech.service.ProductsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.enums.ConditionStatus;
import com.proyecto.fenixtech.model.enums.ListingType;
import com.proyecto.fenixtech.model.enums.ProductStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Products", description = "API para gestión de productos")
@RequestMapping("/api/products")
@RestController
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Products>> findAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findAllProducts());
    }

    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Products> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findById(id));
    }

    @Operation(summary = "Obtener productos por status", description = "Devuelve una lista de productos por su status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito")
    })
    @GetMapping("/status/product")
    public ResponseEntity<List<Products>> findByProductStatus(
            @RequestParam(required = true) ProductStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByProductStatus(status));
    }

    @Operation(summary = "Obtener productos por tipo de venta", description = "Devuelve una lista de productos por su tipo de venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito")
    })
    @GetMapping("/listing-type")
    public ResponseEntity<List<Products>> findByListingType(@RequestParam(required = true) ListingType type) {
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByListingType(type));
    }

    @Operation(summary = "Obtener productos por condicion", description = "Devuelve una lista de productos por su condicion")
    @ApiResponses(value ={  
            @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito")
    })
    @GetMapping("/status/condition")
    public ResponseEntity<List<Products>> findByStatus(@RequestParam(required = true) ConditionStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByStatus(status));
    }

    @Operation(summary = "Obtener productos en funcion del estado, su tipo de venta y su condicion", description = "Devuelve una lista de productos por su condicion, tipo de venta y estado de manera opcional")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito")
    })
    @GetMapping("/filter")
    public ResponseEntity<List<Products>> findByMultipleFilters(
            @RequestParam(required = false) ProductStatus pStatus,
            @RequestParam(required = false) ListingType lType,
            @RequestParam(required = false) ConditionStatus cStatus) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productsService.findByMultipleFilters(pStatus, lType, cStatus));
    }

    @Operation(summary = "Obtener productos por precio mayor que el precio introducido", description = "Devuelve una lista de productos por su precio mayor que el precio introducido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito")
    })
    @GetMapping("/price/greater")
    public ResponseEntity<List<Products>> findByPriceGreaterThan(@RequestParam(required = true) Double price){
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByPriceGreaterThan(price));
    }

    @Operation(summary = "Obtener productos por precio menor que el precio introducido", description = "Devuelve una lista de productos por su precio menor que el precio introducido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito")
    })
    @GetMapping("/price/less")
    public ResponseEntity<List<Products>> findByPriceLessThan( @RequestParam(required = true) Double price){
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByPriceLessThan(price));
    }

    @Operation(summary = "Obtener productos que están en stock", description = "Devuelve una lista de productos que están en stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito")
    })
    @GetMapping("/stock/available")
    public ResponseEntity<List<Products>> findByStockAvailable(){
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByStockAvailable());
    }

    @Operation(summary = "Obtener productos que no están en stock", description = "Devuelve una lista de productos que no están en stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito")
    })
    @GetMapping("/stock/without")
    public ResponseEntity<List<Products>> findWithoutStock(){
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByWithoutStock());
    }

    @Operation(summary = "Obtener productos con stock mayor que el introducido", description = "Devuelve una lista de productos con stock mayor que el introducido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito")
    })
    @GetMapping("/stock/greater")
    public ResponseEntity<List<Products>> findByStockGreaterThan(@RequestParam(required = true) Integer stock){
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByStockGreaterThan(stock));
    }

    @Operation(summary = "Obtener productos por nombre", description = "Devuelve una lista de productos que contengan una cadena de texto en su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos encontrados con éxito")
    })
    @GetMapping("/title")
    public ResponseEntity<List<Products>> findByProductTitle(@RequestParam(required = true) String title){
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByProductTitle(title));
    }

    @Operation(summary = "Obtener productos por categoria", description = "Devuelve una lista de productos por su categoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos encontrados con éxito"),
        @ApiResponse(responseCode = "404", description = "La categoria no existe")
    })
    @GetMapping("/category/{id}")
    public ResponseEntity<List<Products>> findByCategoryId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByCategoryId(id));
    }

    @Operation(summary = "Obtener productos por compñía", description = "Devuelve una lista de productos por su compañía asociada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos encontrados con éxito"),
        @ApiResponse(responseCode = "404", description = "La compañía no existe")
    })
    @GetMapping("/company/{id}")
    public ResponseEntity<List<Products>> findByCompanyId( @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(productsService.findByCompanyId(id));
    }

    @Operation(summary = "Obtener el numero de productos", description = "Devuelve el numero de productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Numero de productos obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = productsService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
