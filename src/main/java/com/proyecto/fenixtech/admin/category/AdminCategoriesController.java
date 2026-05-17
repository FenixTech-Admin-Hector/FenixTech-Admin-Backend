package com.proyecto.fenixtech.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.fenixtech.model.Categories;
import com.proyecto.fenixtech.model.Subcategories;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Admin Categories", description = "API exclusiva para organizar el catálogo de FenixTech")
@RequestMapping("/admin")
@RestController
public class AdminCategoriesController {

    @Autowired
    private AdminCategoriesService adminCategoriesService;

    // ==========================================
    // CATEGORÍAS (PADRES)
    // ==========================================

    @Operation(summary = "Ver todas las categorías", description = "Lista todas las categorías, incluyendo las ocultas.")
    @GetMapping("/categories")
    public ResponseEntity<List<Categories>> findAllCategoriesAdmin() {
        return ResponseEntity.ok(adminCategoriesService.findAllCategories());
    }

    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría raíz.")
    @PostMapping("/categories")
    public ResponseEntity<Categories> createCategoryAdmin(@Valid @RequestBody AdminCategoriesRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminCategoriesService.saveCategory(dto));
    }

    @Operation(summary = "Editar categoría", description = "Cambia el nombre o descripción de una categoría existente.")
    @PutMapping("/categories/{id}")
    public ResponseEntity<Categories> updateCategoryAdmin(@PathVariable Integer id, @Valid @RequestBody AdminCategoriesRequestDTO dto) {
        return ResponseEntity.ok(adminCategoriesService.updateCategory(id, dto));
    }

    @Operation(summary = "Activar/Desactivar Categoría", description = "Cambia el estado is_active sin romper las relaciones.")
    @PutMapping("/categories/{id}/toggle")
    public ResponseEntity<Void> toggleCategoryAdmin(@PathVariable Integer id) {
        adminCategoriesService.toggleCategory(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // SUBCATEGORÍAS (HIJAS)
    // ==========================================

    @Operation(summary = "Ver todas las subcategorías", description = "Lista todas las subcategorías, incluyendo las ocultas.")
    @GetMapping("/subcategories")
    public ResponseEntity<List<Subcategories>> findAllSubcategoriesAdmin() {
        return ResponseEntity.ok(adminCategoriesService.findAllSubcategories());
    }

    @Operation(summary = "Subcategorías por Categoría", description = "Filtro dinámico para obtener las hijas de un padre.")
    @GetMapping("/categories/{categoryId}/subcategories")
    public ResponseEntity<List<Subcategories>> findSubcategoriesByParentAdmin(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(adminCategoriesService.findSubcategoriesByParent(categoryId));
    }

    @Operation(summary = "Crear subcategoría", description = "Crea una nueva subcategoría vinculada a un padre.")
    @PostMapping("/subcategories")
    public ResponseEntity<Subcategories> createSubcategoryAdmin(@Valid @RequestBody AdminSubcategoriesRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminCategoriesService.saveSubcategory(dto));
    }

    @Operation(summary = "Editar subcategoría", description = "Edita los datos o cambia la categoría padre.")
    @PutMapping("/subcategories/{id}")
    public ResponseEntity<Subcategories> updateSubcategoryAdmin(@PathVariable Integer id, @Valid @RequestBody AdminSubcategoriesRequestDTO dto) {
        return ResponseEntity.ok(adminCategoriesService.updateSubcategory(id, dto));
    }

    @Operation(summary = "Activar/Desactivar Subcategoría", description = "Cambia el estado is_active de una subcategoría específica.")
    @PutMapping("/subcategories/{id}/toggle")
    public ResponseEntity<Void> toggleSubcategoryAdmin(@PathVariable Integer id) {
        adminCategoriesService.toggleSubcategory(id);
        return ResponseEntity.noContent().build();
    }
}