package com.proyecto.fenixtech.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.model.Proposals;
import com.proyecto.fenixtech.service.ProposalsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Proposals", description = "API para gestión de propuestas")
@RequestMapping("/api/proposals")
@RestController
public class ProposalsController {
    @Autowired
    private ProposalsService proposalsService;

    @Operation(summary = "Obtener todas las propuestas", description = "Devuelve una lista de todas las propuestas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Propuestas obtenidas con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron propuestas")
    })

    @GetMapping
    public List<Proposals> findAllProposals() {
        return proposalsService.findAllProposals();
    }


    @Operation(summary = "Obtener propuesta por ID", description = "Devuelve una propuesta por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Propuesta obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Propuesta no encontrada")
    })
    @GetMapping("/{id}")
    public Proposals findProposalById(@RequestParam Integer id) {
        return proposalsService.findById(id);
    }
    

    @Operation(summary = "Obtener propuestas por ID de usuario", description = "Devuelve una lista de propuestas asociadas a un ID de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Propuestas obtenidas con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron propuestas para el usuario")
    })
    @GetMapping("/user/{id}")
    public List<Proposals> findProposalsByUserId(@RequestParam Integer id) {
        return proposalsService.findByUserId(id);
    }


    @Operation(summary = "Obtener el numero depropuestas", description = "Devuelve el número total de propuestas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de propuestas obtenido con éxito")
    })
    @GetMapping("/count")
    
    public ResponseEntity<Map<String, Long>> count() {
        Long count = proposalsService.count();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
