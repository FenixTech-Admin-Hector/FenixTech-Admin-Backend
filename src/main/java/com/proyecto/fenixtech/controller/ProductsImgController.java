package com.proyecto.fenixtech.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.model.ProductsImg;
import com.proyecto.fenixtech.service.ProductsImgService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "ProductsImg", description = "API para gestión de imágenes de productos")
@RequestMapping("/productsImg")
@RestController
public class ProductsImgController {
    @Autowired
    private ProductsImgService productsImgService;

    @Operation(summary = "Obtener todas las imágenes de productos", description = "Devuelve una lista de todas las imágenes de productos registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imágenes de productos obtenidas con éxito")
    })
    @GetMapping
    public ResponseEntity<List<ProductsImg>> findAll() {
        return ResponseEntity.ok(productsImgService.findAllProductsImg());
    }

    @Operation(summary = "Obtener imagen por ID", description = "Devuelve una imagen de producto por su ID único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen de producto encontrada con éxito"),
        @ApiResponse(responseCode = "404", description = "Imagen de producto no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductsImg> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(productsImgService.findById(id));
    }
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imágenes de producto encontradas con éxito"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @Operation(summary = "Obtener imágenes por ID de producto", description = "Devuelve una lista de imágenes asociadas a un producto específico")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductsImg>> findByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(productsImgService.findByProductId(productId));
    }

    @Operation(summary = "Contar total de imágenes", description = "Devuelve el número total de imágenes almacenadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de imágenes obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count() {
        Map<String, Long> response = new HashMap<>();
        response.put("cantidad", productsImgService.count());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar una imagen", description = "Elimina una imagen de producto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Imagen eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })   
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        productsImgService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
