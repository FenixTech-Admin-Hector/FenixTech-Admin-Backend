package com.proyecto.fenixtech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType; 
import org.springframework.web.bind.annotation.ModelAttribute;

import com.proyecto.fenixtech.dto.ShippingCarrierRequestDTO;
import com.proyecto.fenixtech.model.ShippingCarriers;
import com.proyecto.fenixtech.service.ShippingCarriersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shipping-carriers")
@Tag(name = "Shipping Carriers", description = "Endpoints para la gestión de empresas de transporte")
public class ShippingCarriersController {

    @Autowired
    private ShippingCarriersService shippingCarriersService;

    @Operation(summary = "Obtener todos los transportistas activos", description = "Retorna una lista de todas las empresas activas de transporte configuradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de transportistas obtenida con éxito"),
    })
    @GetMapping
    public ResponseEntity<List<ShippingCarriers>> getAllActive() {
        return ResponseEntity.ok(shippingCarriersService.findAllActive());
    }

    @Operation(summary = "Obtener todos los transportistas", description = "Retorna una lista de todas las empresas de transporte configuradas.")   
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de transportistas obtenida con éxito")
    })
    @GetMapping("/admin")
    public ResponseEntity<List<ShippingCarriers>> getAll(){
        return ResponseEntity.ok(shippingCarriersService.findAll());
    }

    @Operation(summary = "Obtener transportista por ID", description = "Busca una empresa de transporte específica por su identificador único.")
    @ApiResponse(responseCode = "200", description = "Transportista encontrado")
    @ApiResponse(responseCode = "404", description = "Transportista no encontrado")
    @GetMapping("admin/{id}")
    public ResponseEntity<ShippingCarriers> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(shippingCarriersService.findById(id));
    }

    @Operation(summary = "Obtener transportista activo por ID", description = "Busca una empresa de transporte activa específica por su identificador único.")
    @ApiResponse(responseCode = "200", description = "Transportista encontrado")
    @ApiResponse(responseCode = "404", description = "Transportista no encontrado o inactivo")
    @GetMapping("/{id}")
    public ResponseEntity<ShippingCarriers> getByIdActive(@PathVariable Integer id) {
        return ResponseEntity.ok(shippingCarriersService.findByIdActive(id));
    }


    @Operation(summary = "Crear nuevo transportista", description = "Registra una nueva empresa de transporte en el sistema.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ShippingCarriers> create(@Valid @ModelAttribute ShippingCarrierRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shippingCarriersService.save(dto));
    }

    @Operation(summary = "Actualizar transportista", description = "Modifica los datos de una empresa de transporte existente.")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ShippingCarriers> update(
            @Parameter(description = "ID del transportista a actualizar") @PathVariable Integer id,
            @Valid @ModelAttribute ShippingCarrierRequestDTO dto) {
        return ResponseEntity.ok(shippingCarriersService.update(id, dto));
    }

    @Operation(summary = "Eliminar transportista", description = "Elimina físicamente una empresa de transporte del sistema.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        shippingCarriersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Generar URL de seguimiento", description = "Retorna la URL final de seguimiento combinando el template del carrier con el número de tracking.")
    @GetMapping("/{id}/track/{number}")
    public ResponseEntity<String> getFullTrackingUrl(
            @PathVariable Integer id,
            @PathVariable String number) {
        return ResponseEntity.ok(shippingCarriersService.buildTrackingUrl(id, number));
    }
}