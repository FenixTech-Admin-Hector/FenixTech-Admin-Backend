package com.proyecto.fenixtech.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.proyecto.fenixtech.service.CompaniesService;
import com.proyecto.fenixtech.dto.CompaniesRequestUpdateDTO;
import com.proyecto.fenixtech.model.Companies;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Companies", description = "API para gestión de empresas con métricas de impacto")
@RequestMapping("/companies")
@RestController
public class CompaniesController {

    @Autowired
    private CompaniesService companiesService;

    @Operation(summary = "Obtener todas las empresas activas", description = "Retorna la lista de empresas que tienen is_active = true")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresas activas obtenidas con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Companies>> findAll() {
        return ResponseEntity.ok(companiesService.findAll());
    }

    @Operation(summary = "Obtener todas las empresas (Admin)", description = "Retorna todas las empresas incluyendo las inactivas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista completa de empresas obtenida con éxito")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Companies>> findAllCompanies() {
        return ResponseEntity.ok(companiesService.findAllCompanies());
    }

    @Operation(summary = "Obtener empresa activa por ID", description = "Busca una empresa por su ID siempre que esté activa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresa encontrada con éxito"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada o inactiva")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Companies> findByIdActive(@PathVariable Integer id) {
        return ResponseEntity.ok(companiesService.findByIdActive(id));
    }

    @Operation(summary = "Obtener empresa por ID de usuario", description = "Busca la empresa asociada a un ID de usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresa vinculada al usuario encontrada"),
        @ApiResponse(responseCode = "404", description = "No existe empresa para ese usuario")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Companies> findByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(companiesService.findByUserId(userId));
    }

    @Operation(summary = "Filtrar empresas por impacto", description = "Filtra por reputación y métricas ambientales/sociales del JSON")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultados de búsqueda obtenidos")
    })
    @GetMapping("/search/impact")
    public ResponseEntity<List<Companies>> findByImpactFilters(
            @RequestParam(required = false) Integer minReputation,
            @RequestParam(required = false) Integer maxReputation,
            @RequestParam(required = false) Double minCo2Saved,
            @RequestParam(required = false) Integer minItemsDonated) {
        return ResponseEntity.ok(companiesService.findByImpactFilters(minReputation, maxReputation, minCo2Saved, minItemsDonated));
    }

    @Operation(summary = "Top 3 empresas", description = "Obtiene las 3 empresas con mayor reputación activa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Top 3 obtenido con éxito")
    })
    @GetMapping("/top")
    public ResponseEntity<List<Companies>> findTop3() {
        return ResponseEntity.ok(companiesService.findTop3ByReputationScore());
    }

    @Operation(summary = "Contar empresas activas", description = "Retorna el número total de empresas con estado activo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conteo realizado con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = companiesService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("count_active_companies", count);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar empresa por ID", description = "Actualiza los datos básicos de la empresa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresa actualizada con éxito"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Companies> update(@PathVariable Integer id, @Valid @RequestBody CompaniesRequestUpdateDTO dto) {
        return ResponseEntity.ok(companiesService.update(id, dto));
    }

    @Operation(summary = "Borrado lógico de empresa", description = "Desactiva la empresa y su usuario asociado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Empresa desactivada correctamente"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        companiesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}