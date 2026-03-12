package com.proyecto.fenixtech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.proyecto.fenixtech.dto.SubcategoriesRequestDTO;
import com.proyecto.fenixtech.model.Subcategories;
import com.proyecto.fenixtech.service.SubcategoriesService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Subcategories", description = "API para gestión de subcategorías")
@RequestMapping("/subcategories")
@RestController
public class SubcategoriesController {
    @Autowired 
    private SubcategoriesService subcategoriesService;

    @Operation(summary = "Obtener todas las subcategorías", description = "Devuelve una lista de todas las subcategorías")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subcategorías obtenidas con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Subcategories>> findAllSubcategories() {
        return ResponseEntity.status(HttpStatus.OK).body(subcategoriesService.findAllSubcategories());
    }

    @Operation(summary = "Obtener subcategoría por ID", description = "Devuelve una subcategoría por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subcategoría obtenida con éxito"),
        @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Subcategories> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(subcategoriesService.findByIdActive(id));
    }

    @Operation(summary = "Obtener subcategorías por ID de categoría", description = "Devuelve una lista de subcategorías asociadas a un ID de categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subcategorías obtenidas con éxito"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/category/{id}")
    public ResponseEntity<List<Subcategories>> findByCategoryId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(subcategoriesService.findByCategoryId(id));
    }

    @Operation(summary = "Obtener subcategorías por nombre", description = "Devuelve una lista de subcategorías que contienen el nombre proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subcategorías encontradas con éxito")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Subcategories>> findByName(@RequestParam (required = true) String name) {
        return ResponseEntity.status(HttpStatus.OK).body(subcategoriesService.findByName(name));
    }

    @Operation(summary = "Obtener el numero de subcategorias", description = "Devuelve el numero de subcategorias")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Numero de subcategorias obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = subcategoriesService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Actualizar subcategoría por ID", description = "Actualiza una subcategoría por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subcategoría actualizada con éxito"),
        @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Subcategories> update(@PathVariable Integer id, @Valid @RequestBody SubcategoriesRequestDTO subcategory) {
        Subcategories updatedSubcategory = subcategoriesService.update(id, subcategory);
        return ResponseEntity.status(HttpStatus.OK).body(updatedSubcategory);
    }

    @Operation(summary = "Eliminar subcategoría por ID", description = "Elimina una subcategoría por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Subcategoría eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        subcategoriesService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Crear subcategoría", description = "Crea una nueva subcategoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Subcategoría creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<Subcategories> save(@Valid @RequestBody SubcategoriesRequestDTO subcategory) {
        Subcategories subcategories = subcategoriesService.save(subcategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(subcategories);
    }



}
