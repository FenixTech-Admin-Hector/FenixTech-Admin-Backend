package com.proyecto.fenixtech.admin.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.enums.ProductStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Products", description = "API exclusiva para la moderación del catálogo por parte del Administrador")
@RequestMapping("/admin/products")
@RestController
public class AdminProductsController {
    // ... el resto del código se queda igual ...

    @Autowired
    private AdminProductsService adminProductsService;

    @Operation(summary = "Mostrar y filtrar todo el catálogo", description = "Lista todos los productos sin importar su estado, permitiendo filtros.")
    @GetMapping
    public ResponseEntity<List<Products>> findProductsAdmin(
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) Integer subcategoryId,
            @RequestParam(required = false) Integer companyId,
            @RequestParam(required = false) String title) {
        return ResponseEntity.ok(adminProductsService.findProductsForAdmin(status, subcategoryId, companyId, title));
    }

    @Operation(summary = "Ocultar producto (Banear)", description = "Cambia el estado a HIDDEN y lo elimina de los carritos de compra activos.")
    @PutMapping("/{id}/hide")
    public ResponseEntity<Void> hideProduct(@PathVariable Integer id) {
        adminProductsService.hideProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restaurar producto", description = "Restaura un producto oculto a ACTIVE o SOLD_OUT dependiendo de su stock actual.")
    @PutMapping("/{id}/unhide")
    public ResponseEntity<Void> unhideProduct(@PathVariable Integer id) {
        adminProductsService.unhideProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar imagen", description = "Permite al administrador borrar fotos inapropiadas de un producto.")
    @DeleteMapping("/images/{imgId}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable Integer imgId) {
        adminProductsService.deleteProductImage(imgId);
        return ResponseEntity.noContent().build();
    }
}