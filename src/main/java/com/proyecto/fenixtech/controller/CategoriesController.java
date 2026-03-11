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

import com.proyecto.fenixtech.service.CategoriesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.proyecto.fenixtech.dto.CategoriesRequestDTO;
import com.proyecto.fenixtech.model.Categories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Categories", description = "API para gestión de categorías")
@RequestMapping("/categories")
@RestController
public class CategoriesController {
    @Autowired
    private CategoriesService categoriesService;

    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista de todas las categorías")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categorías obtenidas con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Categories>> findAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoriesService.findAllCategories());
    }

    @Operation(summary = "Obtener categoría por ID", description = "Devuelve una categoría por su ID")  
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría obtenida con éxito"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categories> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriesService.findById(id));
    }

    @Operation(summary = "Obtener categoría por nombre", description = "Devuelve una categoría por su nombre")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Categoría encontrada con éxito"),
    })
    @GetMapping("/name")
    public ResponseEntity<Categories> findByCategoryName(@RequestParam (required = true) String name) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriesService.findByCategoryName(name));
    }

    @Operation(summary = "Obtener el numero de categorias", description = "Devuelve el numero de categorias")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Numero de categorias obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = categoriesService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Crear una nueva categoría", description = "Crea una nueva categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<Categories> save(@Valid @RequestBody CategoriesRequestDTO category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriesService.save(category));
    }

    @Operation(summary = "Eliminar una categoría por ID", description = "Elimina una categoría por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoría eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        categoriesService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Actualizar una categoría por ID", description = "Actualiza una categoría por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada con éxito"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Categories> update(@PathVariable Integer id, @Valid @RequestBody CategoriesRequestDTO category) {
        Categories updatedCategory = categoriesService.update(id, category);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }





}
