package com.proyecto.fenixtech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.fenixtech.model.Shipments;
import com.proyecto.fenixtech.model.enums.ShipmentStatus;
import com.proyecto.fenixtech.service.ShipmentsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Shipments", description = "API para gestión de envíos")
@RequestMapping("/api/shipments")
@RestController
public class ShipmentsController {
    @Autowired
    private ShipmentsService shipmentsService;

    @Operation(summary = "Obtener todos los envíos", description = "Devuelve una lista de todos los envíos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Envíos obtenidos con éxito")
    })
    @GetMapping
    public ResponseEntity<List<Shipments>> findAllShipments() {
        return ResponseEntity.status(HttpStatus.OK).body(shipmentsService.findAllShipments());
    }

    @Operation(summary = "Obtener envío por ID", description = "Devuelve un envío por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Envío obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Shipments> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(shipmentsService.findById(id));
    }

    @Operation(summary = "Obtener envíos por ID de pedido", description = "Devuelve una lista de envíos asociados a un ID de pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Envíos obtenidos con éxito"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/order/{id}")
    public ResponseEntity<List<Shipments>> findByOrderId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(shipmentsService.findByOrderId(id));
    }

    @Operation(summary = "Obtener envíos por filtros", description = "Devuelve una lista de envíos filtrados por dirección, ciudad, código postal, país, seguimiento, transportista y estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Envíos obtenidos con éxito")
    })
    @GetMapping("/filters")
    public ResponseEntity<List<Shipments>> findByConditions(
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String zipCode,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String trackingNumber,
            @RequestParam(required = false) String carrier,
            @RequestParam(required = false) ShipmentStatus status) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(shipmentsService.findByConditions(street, city, zipCode, country, trackingNumber, carrier, status));
    }

}
