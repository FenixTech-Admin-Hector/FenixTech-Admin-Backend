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
import com.proyecto.fenixtech.service.AddressesService;
import com.proyecto.fenixtech.dto.AddressRequestDTO;
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
@RequestMapping("/addresses")
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
        public ResponseEntity<Addresses> findById(@PathVariable Integer id) {
                return ResponseEntity.status(HttpStatus.OK).body(addressesService.findById(id));
        }

        @Operation(summary = "Obtener direcciones por ID de usuario", description = "Devuelve una lista de direcciones asociadas a un ID de usuario")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Direcciones obtenidas con éxito"),
                        @ApiResponse(responseCode = "404", description = "No se encontraron direcciones para el usuario")
        })
        @GetMapping("/user/{id}")
        public ResponseEntity<List<Addresses>> findByUserId(@PathVariable Integer id) {
                return ResponseEntity.status(HttpStatus.OK).body(addressesService.findByUserId(id));
        }

        @Operation(summary = "Obtener direcciones por filtros", description = "Devuelve una lista de direcciones filtradas por ciudad, calle, región y país")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Direcciones obtenidas con éxito")
        })
        @GetMapping("/filters")
        public ResponseEntity<List<Addresses>> findByConditions(
                        @RequestParam(required = false) String street,
                        @RequestParam(required = false) String city,
                        @RequestParam(required = false) String region,
                        @RequestParam(required = false) String country,
                        @RequestParam(required = false) String zipCode) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(addressesService.findByConditions(street, city, region, country, zipCode));
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

        @Operation(summary = "Crear una dirección", description = "Crea una nueva dirección")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Dirección creada con éxito"),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
        })
        @PostMapping
        public ResponseEntity<Addresses> save(@Valid @RequestBody AddressRequestDTO dto) {
                return ResponseEntity.status(HttpStatus.CREATED).body(addressesService.save(dto));
        }

        @Operation(summary = "Eliminar una dirección por ID", description = "Elimina una dirección por su ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Dirección eliminada con éxito"),
                        @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
                addressesService.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        @Operation(summary = "Actualizar una dirección por ID", description = "Actualiza una dirección por su ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Dirección actualizada con éxito"),
                        @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
        })
        @PutMapping("/{id}")
        public ResponseEntity<Addresses> update(@PathVariable Integer id, @Valid @RequestBody AddressRequestDTO dto) {
                Addresses updated = addressesService.update(id, dto);
                return ResponseEntity.ok(updated);
        }

}