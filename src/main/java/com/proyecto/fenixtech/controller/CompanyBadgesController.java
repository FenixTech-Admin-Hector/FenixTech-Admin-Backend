package com.proyecto.fenixtech.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.model.CompanyBadges;
import com.proyecto.fenixtech.service.CompanyBadgesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "CompanyBadges", description = "API para gestión de insignias de empresas")
@RequestMapping("/api/company_badges")
@RestController
public class CompanyBadgesController {
    @Autowired
    private CompanyBadgesService companyBadgesService;

    @Operation(summary = "Obtener todas las insignias de empresas", description = "Devuelve una lista de todas las insignias de empresas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignias de empresas obtenidas con éxito")
    })
    @GetMapping
    public ResponseEntity<List<CompanyBadges>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(companyBadgesService.findAll());
    }

    @Operation(summary = "Obtener insignia de empresa por ID", description = "Devuelve una insignia de empresa por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insignia de empresa obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Insignia de empresa no encontrada")
    })
    @GetMapping("/{companyId}/{badgeId}")
    public ResponseEntity<CompanyBadges> findById(@PathVariable Integer companyId, @PathVariable Integer badgeId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyBadgesService.findById(companyId, badgeId));
    }

    @Operation(summary = "Obtener insignias de empresas por ID de empresa", description = "Devuelve una lista de insignias de empresas asociadas a un ID de empresa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Insignias de empresas obtenidas con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontraron insignias de empresas para la empresa")
    })
    @GetMapping("/company/{id}")
    public ResponseEntity<List<CompanyBadges>> findByCompanyId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(companyBadgesService.findByCompanyId(id));
    }

    @Operation(summary = "Obtener insignias de empresas por fecha de asignación", description = "Devuelve una lista de insignias de empresas filtradas por fecha de asignación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Insignias de empresas obtenidas con éxito")
    })
    @GetMapping("/awarded_at")
    public ResponseEntity<List<CompanyBadges>> findByAwardedAtBetween(@RequestParam (required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam (required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(companyBadgesService.findByAwardedAtBetween(startDate, endDate));
    }



    @Operation(summary = "Obtener el número total de insignias de empresas", description = "Devuelve el número total de insignias de empresas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de insignias de empresas obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = companyBadgesService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
        





}
