package com.proyecto.fenixtech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.fenixtech.service.AddressesService;
import com.proyecto.fenixtech.model.Addresses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Addresses", description = "API para gestión de direcciones")
@RequestMapping("/api/addresses")
@RestController
public class AddressesController {
    @Autowired
    private AddressesService addressesService;

    @Operation(summary = "Obtener todas las direcciones", description = "Devuelve una lista de todas las direcciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Direcciones obtenidas con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Addresses>> findAllAddresses() {
        return ResponseEntity.status(HttpStatus.OK).body(addressesService.findAllAddresses());
    }

    @Operation(summary = "Obtener dirección por ID", description = "Devuelve una dirección por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dirección obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Addresses> findById(@Valid @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(addressesService.findById(id));
    }

    @Operation(summary = "Obtener direcciones por ID de usuario", description = "Devuelve una lista de direcciones asociadas a un ID de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Direcciones obtenidas con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron direcciones para el usuario")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Addresses>> findByUserId(@Valid @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(addressesService.findByUserId(id));
    }

    @Operation(summary = "Obtener direcciones por ciudad", description = "Devuelve una lista de direcciones filtradas por ciudad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Direcciones obtenidas con éxito")
    })
    @GetMapping("/city")
    public ResponseEntity<List<Addresses>> findByCity(@Valid @RequestParam(required = true) String city) {
        return ResponseEntity.status(HttpStatus.OK).body(addressesService.findByCity(city));
    }

    @Operation(summary = "Obtener direcciones por ciudad", description = "Devuelve una lista de direcciones filtradas por ciudad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Direcciones obtenidas con éxito")
    })
    @GetMapping("/region")
    public ResponseEntity<List<Addresses>> findByRegion(@Valid @RequestParam(required = true) String region) {
        return ResponseEntity.status(HttpStatus.OK).body(addressesService.findByRegion(region));
    }

    @Operation(summary = "Obtener direcciones por país", description = "Devuelve una lista de direcciones filtradas por país")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Direcciones obtenidas con éxito")
    })
    @GetMapping("/country")
    public ResponseEntity<List<Addresses>> findByCountry(@Valid @RequestParam(required = true) String country) {
        return ResponseEntity.status(HttpStatus.OK).body(addressesService.findByCountry(country));
    }

    @Operation(summary = "Obtener direcciones por código postal", description = "Devuelve una lista de direcciones filtradas por código postal")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Direcciones obtenidas con éxito")
    })
    @GetMapping("/zip_code")
    public ResponseEntity<List<Addresses>> findByZipCode(@Valid @RequestParam(required = true) String zipCode) {
        return ResponseEntity.status(HttpStatus.OK).body(addressesService.findByZipCode(zipCode));
    }

    @Operation(summary = "Obtener el numero de direcciones", description = "Devuelve el numero de direcciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Numero de direcciones obtenido con éxito")
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {
        Long count = addressesService.count();
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

} 