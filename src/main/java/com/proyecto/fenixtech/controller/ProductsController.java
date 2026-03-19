package com.proyecto.fenixtech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.fenixtech.service.ProductsService;
import org.springframework.http.MediaType; 
import org.springframework.web.bind.annotation.ModelAttribute;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.proyecto.fenixtech.dto.ProductRequestUpdateDTO;
import com.proyecto.fenixtech.dto.ProductsRequestPostDTO;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.enums.ConditionStatus;
import com.proyecto.fenixtech.model.enums.ListingType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Products", description = "API para gestión de productos")
@RequestMapping("/products")
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

        @Operation(summary = "Obtener productos con filtros", description = "Devuelve una lista de productos con filtros aplicados (stock, tipo de venta, estado del producto y precio")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Productos encontrados con éxito")
        })
        @GetMapping("/conditions")
        public ResponseEntity<List<Products>> findByUltimateFilter(
                        @RequestParam(required = false) ListingType lType,
                        @RequestParam(required = false) ConditionStatus cStatus,
                        @RequestParam(required = false, defaultValue = "0.0") Double minPrice,
                        @RequestParam(required = false) Double maxPrice,
                        @RequestParam(required = false, defaultValue = "0") Integer minStock,
                        @RequestParam(required = false) Integer maxStock,
                        @RequestParam(required = false) String location) {
                return ResponseEntity.status(HttpStatus.OK).body(
                                productsService.findByConditions(lType, cStatus, minPrice, maxPrice, minStock, maxStock,
                                                location));
        }

        @Operation(summary = "Obtener productos por nombre", description = "Devuelve una lista de productos que contengan una cadena de texto en su nombre")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Productos encontrados con éxito")
        })
        @GetMapping("/title")
        public ResponseEntity<List<Products>> findByProductTitle(@RequestParam(required = true) String title) {
                return ResponseEntity.status(HttpStatus.OK).body(productsService.findByProductTitle(title));
        }

        @Operation(summary = "Obtener productos por subcategoria", description = "Devuelve una lista de productos por su subcategoria")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Productos encontrados con éxito"),
                        @ApiResponse(responseCode = "404", description = "La categoria no existe")
        })
        @GetMapping("/subcategory/{id}")
        public ResponseEntity<List<Products>> findBySubcategoryId(@PathVariable Integer id) {
                return ResponseEntity.status(HttpStatus.OK).body(productsService.findBySubcategoryId(id));
        }

        @Operation(summary = "Obtener productos por compañía", description = "Devuelve una lista de productos por su compañía asociada")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Productos encontrados con éxito"),
                        @ApiResponse(responseCode = "404", description = "La compañía no existe")
        })
        @GetMapping("/company/{id}")
        public ResponseEntity<List<Products>> findByCompanyId(@PathVariable Integer id) {
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

        @Operation(summary = "Eliminar producto por ID", description = "Elimina un producto por su ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Producto eliminado con éxito"),
                        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
                productsService.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        @Operation(summary = "Actualizar un producto", description = "Actualiza un producto existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Producto actualizado con éxito"),
                        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
        })
        @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Products> update(
                        @PathVariable Integer id,
                        @Valid @ModelAttribute ProductRequestUpdateDTO dto) {

                return ResponseEntity.ok(productsService.update(id, dto));
        }

        @Operation(summary = "Crear un producto", description = "Crea un nuevo producto")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Producto creado con éxito"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos")
        })
        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Products> createProduct(@Valid @ModelAttribute ProductsRequestPostDTO product) {
                Products savedProduct = productsService.save(product);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        }

}
