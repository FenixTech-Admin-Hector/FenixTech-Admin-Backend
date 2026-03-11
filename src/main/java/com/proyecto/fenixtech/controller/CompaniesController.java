package com.proyecto.fenixtech.controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.service.CompaniesService;
import com.proyecto.fenixtech.dto.CompaniesRequestUpdateDTO;
import com.proyecto.fenixtech.model.Companies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;



@Tag(name = "Companies", description = "API para gestión de empresas")
@RequestMapping("/companies")
@RestController
public class CompaniesController {
    @Autowired 
    private CompaniesService companiesService;

    @Operation(summary = "Obtener todas las empresas", description = "Devuelve una lista de todas las empresas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresas obtenidas con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Companies>> findAllCompanies(){
        return ResponseEntity.status(HttpStatus.OK).body(companiesService.findAllCompanies());
    }

    @Operation(summary = "Obtener empresa por ID", description = "Devuelve una empresa por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresa obtenida con éxito"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Companies> findById(@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(companiesService.findById(id));
    }

    @Operation(summary = "Obtener empresa por ID de usuario", description = "Devuelve una empresa por su ID de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresa encontrada con éxito"),
        @ApiResponse(responseCode = "404", description = "El usuario no está asociado a ninguna empresa")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<Companies> findByUserId( @PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(companiesService.findByUserId(id));
    }

    @Operation(summary = "Obtener empresas por nombre", description = "Devuelve una lista de empresas que contengan una cadena de texto en su nombre")
    @ApiResponses(value =  {
        @ApiResponse(responseCode = "200", description = "Empresas encontradas con éxito")
    })
    @GetMapping("/name")
    public ResponseEntity<List<Companies>> findByName( @RequestParam(required = true) String name){
        return ResponseEntity.status(HttpStatus.OK).body(companiesService.findByCompanyName(name));
    }

    @Operation(summary = "Obtener empresas por una serie de filtros", description ="Devuelve una lista de empresas en función de sus puntos de reputación y sus métricas de impacto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresas encontradas con éxito")
    })
    @GetMapping("/filters")
    public ResponseEntity<List<Companies>> findByImpactFilters(@RequestParam(required = false, defaultValue = "0") Integer minReputation,
    @RequestParam(required = false) Integer maxReputation, @RequestParam(required = false, defaultValue = "0.0") Double minCo2Saved, @RequestParam(required = false, defaultValue = "0") Integer minItemsDonated){
        return ResponseEntity.status(HttpStatus.OK).body(companiesService.findByImpactFilters(minReputation, maxReputation, minCo2Saved, minItemsDonated));
    }
    @GetMapping("/top3")
    public ResponseEntity<List<Companies>> findTop3ByReputationScore(){
        return ResponseEntity.status(HttpStatus.OK).body(companiesService.findTop3ByReputationScore());
    }


    @Operation(summary = "Obtener el numero de empresas", description = "Devuelve el numero de empresas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Numero de empresas obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = companiesService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Eliminar una empresa por ID", description = "Elimina una empresa por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Empresa eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id){
        companiesService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Actualizar una empresa por ID", description = "Actualiza una empresa por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresa actualizada con éxito"),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Companies> update(@PathVariable Integer id, @Valid @RequestBody CompaniesRequestUpdateDTO company){
        Companies updatedCompany = companiesService.update(id, company);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCompany);
    }





    








}
